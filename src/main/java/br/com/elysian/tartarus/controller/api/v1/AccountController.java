package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.v1.*;
import br.com.elysian.tartarus.dto.v1.patch.PasswordDTO;
import br.com.elysian.tartarus.dto.v1.post.RegisterDTO;
import br.com.elysian.tartarus.dto.v1.put.AccountDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Role;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.AccountService;
import br.com.elysian.tartarus.service.captcha.CaptchaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api/v1/account")
@Tag(name = "Account", description = "Manage the Account")
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


    @Transactional
    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@Valid @RequestBody RegisterDTO registerDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
    public ResponseEntity<Account> put(@RequestBody AccountDTO accountDTO) {
        return ResponseEntity.ok(
                accountService.put(accountDTO)
        );
    }


    @PostMapping("/confirm")
    public ResponseEntity<TokenDTO> confirm(@RequestParam("sequenceToken") String sequenceToken) throws Exception {
        return ResponseEntity.ok(
                accountService.confirmAccount(accountService.getAccountLoggedIn(), sequenceToken)
        );
    }


    @PatchMapping("/profile-pic")
    public ResponseEntity<Void> updateProfilePic(@RequestBody String dataPic) {
        accountService.updateProfilePic(dataPic);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/password")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TokenDTO> updatePassword(@Valid @RequestBody PasswordDTO passwordDTO, HttpServletRequest request, HttpServletResponse response) throws NewPasswordEqualsOldPasswordException, MismatchedPasswordsException, InvalidUsernameOrPasswordException, ReCaptchaUnavailableException, ReCaptchaInvalidException {
        if (!activeProfile.equals("dev")) {
            String captchaResponse = request.getHeader("g-recaptcha-response");
            captchaService.processResponse(captchaResponse);
        }

        Account account = accountService.updatePassword(passwordDTO);

        TokenDTO token = authService.getAuthorization(account);
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getRoles() {
            return ResponseEntity.ok(
                    accountService.getRoles()
            );
    }


    @PatchMapping("/roles")
    public ResponseEntity<TokenDTO> patchRole(@RequestParam("roleId") String roleId, HttpServletResponse response) throws InvalidUsernameOrPasswordException, RoleNotFoundException {
        accountService.patchRole(roleId);

        TokenDTO token = authService.getAuthorizationByCurrentAuthentication();
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }

}
