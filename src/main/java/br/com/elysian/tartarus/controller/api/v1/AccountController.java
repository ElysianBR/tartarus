package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.v1.*;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.AccountService;
import br.com.elysian.tartarus.service.captcha.CaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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


    @GetMapping()
    @Transactional
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<Account> get() {
        return ResponseEntity.ok(
                accountService.getAccountLoggedIn()
        );
    }


    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@Valid @RequestBody RegisterDTO registerDTO, HttpServletRequest request, HttpServletResponse response) throws ReCaptchaException, EmailAlreadyRegisteredException, InvalidUsernameOrPasswordException {
        if (!activeProfile.equals("dev")) {
            String captchaResponse = request.getHeader("g-recaptcha-response");
            captchaService.processResponse(captchaResponse);
        }

        Account account = accountService.register(registerDTO);
        TokenDTO token = authService.getAuthorization(account);
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


    @PutMapping()
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<Account> put(@Valid @RequestBody PutAccountDTO accountDTO) {
        return ResponseEntity.ok(
                accountService.put(accountDTO)
        );
    }


    @PostMapping("/confirm")
    public ResponseEntity<SuccessDTO> confirm(@RequestParam("sequenceToken") String sequenceToken) {
        return ResponseEntity.ok(
                accountService.confirmAccount(sequenceToken)
        );
    }


    @PatchMapping("/password")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TokenDTO> updatePassword(@Valid @RequestBody PatchPasswordDTO patchPasswordDTO, HttpServletRequest request, HttpServletResponse response) throws NewPasswordEqualsOldPasswordException, MismatchedPasswordsException, InvalidUsernameOrPasswordException, ReCaptchaUnavailableException, ReCaptchaInvalidException {
        if (!activeProfile.equals("dev")) {
            String captchaResponse = request.getHeader("g-recaptcha-response");
            captchaService.processResponse(captchaResponse);
        }

        Account account = accountService.updatePassword(patchPasswordDTO);

        TokenDTO token = authService.getAuthorization(account);
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


}
