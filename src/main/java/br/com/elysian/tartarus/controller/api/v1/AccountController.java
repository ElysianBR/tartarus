package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.v1.*;
import br.com.elysian.tartarus.exception.*;
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
    public ResponseEntity<AccountDTO> getAccount() {
        return ResponseEntity.ok(
                new AccountDTO(
                        accountService.getAccountLoggedIn()
                )
        );
    }


    @PostMapping("/create")
    public ResponseEntity<TokenDTO> createAccount(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request, HttpServletResponse response) throws ReCaptchaException, EmailAlreadyRegisteredException, InvalidUsernameOrPasswordException {
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
    @PostMapping("/confirm")
    public ResponseEntity<SuccessDTO> confirmAccount(@RequestParam("sequenceToken") String sequenceToken) {
        return ResponseEntity.ok(
                accountService.confirmAccount(sequenceToken)
        );
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @PostMapping("/update")
    public ResponseEntity<AccountDTO> update(@Valid @RequestBody SaveAccountDTO accountDTO) {
        return ResponseEntity.ok(
                new AccountDTO(
                        accountService.saveAccount(accountDTO)
                )
        );
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    @PostMapping("/update-password")
    public ResponseEntity<TokenDTO> updatePassword(@Valid @RequestBody SavePasswordDTO savePasswordDTO, HttpServletRequest request, HttpServletResponse response) throws NewPasswordEqualsOldPasswordException, MismatchedPasswordsException, InvalidUsernameOrPasswordException, ReCaptchaUnavailableException, ReCaptchaInvalidException {
        if (!activeProfile.equals("dev")) {
            String captchaResponse = request.getHeader("g-recaptcha-response");
            captchaService.processResponse(captchaResponse);
        }

        Account account = accountService.updatePassword(savePasswordDTO);

        TokenDTO token = authService.getAuthorization(account);
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


}
