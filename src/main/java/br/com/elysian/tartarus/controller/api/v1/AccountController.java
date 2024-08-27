package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.v1.AccountLoggedInDTO;
import br.com.elysian.tartarus.dto.v1.RegistrationDTO;
import br.com.elysian.tartarus.dto.v1.SuccessDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.exception.EmailAlreadyRegisteredException;
import br.com.elysian.tartarus.exception.ReCaptchaException;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.AccountService;
import br.com.elysian.tartarus.service.captcha.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final CaptchaService captchaService;
    private final AccountService accountService;
    private final AuthService authService;

    @Autowired
    public AccountController(CaptchaService captchaService, AccountService accountService, AuthService authService) {
        this.captchaService = captchaService;
        this.accountService = accountService;
        this.authService = authService;
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @GetMapping()
    public ResponseEntity<AccountLoggedInDTO> getAccount() {
        return ResponseEntity.ok(
                new AccountLoggedInDTO(
                        accountService.getAccountLoggedIn()
                )
        );
    }


    @PostMapping("/create-account")
    public ResponseEntity<TokenDTO> createAccount(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request, HttpServletResponse response) throws ReCaptchaException, EmailAlreadyRegisteredException {
        if (!activeProfile.equals("dev")) {
            String captchaResponse = request.getHeader("g-recaptcha-response");
            captchaService.processResponse(captchaResponse);
        }

        Account account = accountService.saveUser(registrationDTO);
        TokenDTO token = authService.getAuthorization(account);
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/confirm-account")
    public ResponseEntity<SuccessDTO> confirmAccount(@RequestParam("sequenceToken") String sequenceToken) {
        return ResponseEntity.ok(
                accountService.confirmAccount(sequenceToken)
        );
    }


}
