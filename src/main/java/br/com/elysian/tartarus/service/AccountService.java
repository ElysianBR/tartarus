package br.com.elysian.tartarus.service;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.dto.v1.*;
import br.com.elysian.tartarus.dto.v1.patch.PasswordDTO;
import br.com.elysian.tartarus.dto.v1.post.RegisterDTO;
import br.com.elysian.tartarus.dto.v1.put.AccountDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.model.pk.AccountRolePK;
import br.com.elysian.tartarus.model.pk.ConfirmationPK;
import br.com.elysian.tartarus.repository.*;
import br.com.elysian.tartarus.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {

    private final MessageService messageService;
    private final AccountDAO accountDAO;
    private final AccountRoleDAO accountRoleDAO;
    private final RoleDAO roleDAO;
    private final ConfirmationDAO confirmationDAO;
    private final ConfirmationTypeDAO confirmationTypeDAO;
    private final AuthService authService;


    @Autowired
    public AccountService(MessageService messageService, AuthService authService, AccountDAO accountDAO, AccountRoleDAO accountRoleDAO, RoleDAO roleDAO, ConfirmationDAO confirmationMailRep, ConfirmationTypeDAO confirmationTypeDAO) {
        this.messageService = messageService;
        this.accountDAO = accountDAO;
        this.accountRoleDAO = accountRoleDAO;
        this.roleDAO = roleDAO;
        this.confirmationDAO = confirmationMailRep;
        this.confirmationTypeDAO = confirmationTypeDAO;
        this.authService = authService;
    }

    public Account register(RegisterDTO registerDTO) throws Exception {
        if (accountDAO.existsByEmail(registerDTO.getEmail())) {
            throw new EmailAlreadyRegisteredException(String.format(messageService.getMessage("tartarus.validations.email-already-registered"), registerDTO.getEmail()));
        }

        Account account = accountDAO.save(new Account(registerDTO));
        ConfirmationType confirmationType = confirmationTypeDAO.findById(1).get();
        Confirmation confirmationMail = new Confirmation(account, confirmationType);
        confirmationDAO.save(confirmationMail);


        // TODO: Enquanto não é habilitada a confirmação por e-mail, a confrimação é feita automaticamente.
        confirmAccount(account, confirmationMail.getSequence());

//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(user.getEmail());
//        mailMessage.setSubject("Complete Registration on Elysian!");
//        mailMessage.setText("To confirm your account, please click here : " +
//            "http://localhost:8085/confirm-account?token="+confirmationMail.getSequence());
//        emailService.sendEmail(mailMessage);

        return account;
    }


    public TokenDTO confirmAccount(Account account, String sequenceToken) throws Exception {
        // TODO: Implementar exceptions mais precisas

        Optional<Confirmation> confirmation = confirmationDAO.findById(
                new ConfirmationPK(
                        account,
                        confirmationTypeDAO.findById(1).get()
                )
        );

        if (confirmation.isPresent()) {

            Confirmation confirmationMail = confirmation.get();

            Date expiration = confirmationMail.getExpiration();
            if (expiration.after(new Date())) {

                String sequence = confirmationMail.getSequence();
                Date confirmationDate = confirmationMail.getDateConfirmation();
                if (sequence.equals(sequenceToken) && confirmationDate == null) {

                    confirmationMail.setDateConfirmation(new Date());
                    this.confirmationDAO.save(confirmationMail);

                    Role roleUser = roleDAO.findById("ACCOUNT").get();
                    accountRoleDAO.save(new AccountRole(
                            account,
                            roleUser,
                            true
                    ));

                    account.setActive(true);
                    this.accountDAO.save(account);

                    return authService.getAuthorization(account);
                } else {
                    throw new Exception(messageService.getMessage("tartarus.confirmation.invalid-token"));
                }

            } else {
                throw new Exception(messageService.getMessage("tartarus.confirmation.expired-token"));
            }
        }

        throw new Exception(messageService.getMessage("tartarus.confirmation.not-found-token"));
    }


    public Account getAccountLoggedIn() {
        AuthenticationAccount authentication = AuthService.getCurrentAuthentication();
        Long idUser = (Long) authentication.getPrincipal();

        return accountDAO.findById(idUser).orElse(null);
    }


    public Account put(AccountDTO accountDTO) {
        Account account = this.getAccountLoggedIn();
        account.setAccountFromDTO(accountDTO);
        return accountDAO.save(account);
    }


    public Account updatePassword(PasswordDTO passwordDTO) throws MismatchedPasswordsException, NewPasswordEqualsOldPasswordException {
        Account account = this.getAccountLoggedIn();

        String oldPassword = passwordDTO.getOldPassword();
        String newPassword = passwordDTO.getNewPassword();


        if (!oldPassword.equals(newPassword)) {

            if (PasswordUtils.verifyPassword(oldPassword, account.getPassword())) {
                account.setPassword(PasswordUtils.hashPassword(newPassword));

                return accountDAO.save(account);
            }

            throw new MismatchedPasswordsException(messageService.getMessage("tartarus.validations.mismatched-passwords"));

        }

        throw new NewPasswordEqualsOldPasswordException(messageService.getMessage("tartarus.validations.old-password-equals-new-password"));

    }

    public Set<Role> getRoles() {
        return getAccountLoggedIn().getActiveRoles();
    }


    public void patchRole(String roleId) throws RoleNotFoundException {

        Optional<Role> role = roleDAO.findById(roleId);

        if (role.isPresent()) {
            Account account = getAccountLoggedIn();

            boolean found = accountRoleDAO.existsById(
                    new AccountRolePK(
                            account,
                            role.get()
                    )
            );


            if (found) {
                AuthenticationAccount authenticationAccount = AuthService.getCurrentAuthentication();

                authenticationAccount.setSelectedGranted(
                        new SimpleGrantedAuthority(roleId)
                );
                return;
            }

        }

        throw new RoleNotFoundException(String.format(messageService.getMessage("tartarus.role.not-found"), roleId));

    }


}
