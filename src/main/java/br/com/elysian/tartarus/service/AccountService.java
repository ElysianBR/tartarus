package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.dto.RegistrationDTO;
import br.com.elysian.tartarus.dto.SuccessDTO;
import br.com.elysian.tartarus.exception.EmailAlreadyRegisteredException;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Confirmation;
import br.com.elysian.tartarus.model.ConfirmationType;
import br.com.elysian.tartarus.model.Role;
import br.com.elysian.tartarus.repository.ConfirmationDAO;
import br.com.elysian.tartarus.repository.ConfirmationTypeDAO;
import br.com.elysian.tartarus.repository.RoleDAO;
import br.com.elysian.tartarus.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {

    private final MessageService messageService;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final ConfirmationDAO confirmationDAO;
    private final ConfirmationTypeDAO confirmationTypeDAO;


    @Autowired
    public AccountService(MessageService messageService, UserDAO userDAO, RoleDAO roleDAO, ConfirmationDAO confirmationMailRep, ConfirmationTypeDAO confirmationTypeDAO) {
        this.messageService = messageService;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.confirmationDAO = confirmationMailRep;
        this.confirmationTypeDAO = confirmationTypeDAO;
    }

    public Account saveUser(RegistrationDTO registrationDTO) throws EmailAlreadyRegisteredException {
        if (userDAO.existsByEmail(registrationDTO.getEmail())) {
            throw new EmailAlreadyRegisteredException(String.format(messageService.getMessage("tartarus.validations.email.already-registered"), registrationDTO.getEmail()));
        }

        Account account = userDAO.save(new Account(registrationDTO));
        ConfirmationType confirmationType = confirmationTypeDAO.findById(1).get();
        Confirmation confirmationMail = new Confirmation(account, confirmationType);
        confirmationDAO.save(confirmationMail);

//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(user.getEmail());
//        mailMessage.setSubject("Complete Registration on Elysian!");
//        mailMessage.setText("To confirm your account, please click here : " +
//            "http://localhost:8085/confirm-account?token="+confirmationMail.getSequence());
//        emailService.sendEmail(mailMessage);

        return account;
    }


    public SuccessDTO confirmAccount(String sequenceToken) {
        Account account = this.getUserLogged();
        Optional<Confirmation> optionalConfirmation = account.getEnableConfirmation();
        if (optionalConfirmation.isPresent()) {

            Confirmation confirmationMail = optionalConfirmation.get();

            Date expiration = confirmationMail.getExpiration();
            if (expiration.after(new Date())) {

                String sequence = confirmationMail.getSequence();
                Date confirmationDate = confirmationMail.getDateConfirmation();
                if (sequence.equals(sequenceToken) && confirmationDate == null) {

                    confirmationMail.setDateConfirmation(new Date());
                    this.confirmationDAO.save(confirmationMail);
                    account.setActive(true);
                    Set<Role> roles = account.getAccountRoles();
                    Role roleUser = roleDAO.findById("ACCOUNT").get();
                    roles.add(roleUser);
                    account.setAccountRoles(roles);
                    this.userDAO.save(account);
                    return new SuccessDTO(true, messageService.getMessage("tartarus.confirmation.accepted-token"));

                } else {
                    return new SuccessDTO(false, messageService.getMessage("tartarus.confirmation.invalid-token"));
                }

            } else {
                return new SuccessDTO(false, messageService.getMessage("tartarus.confirmation.expired-token"));
            }
        }

        return new SuccessDTO(false, messageService.getMessage("tartarus.confirmation.not-found-token"));
    }


    public Account getUserLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            Long idUser = (Long) authentication.getPrincipal();
            Optional<Account> user = userDAO.findById(idUser);
            if (user.isPresent()) {
                return user.get();
            }
        }
        return null;
    }
}
