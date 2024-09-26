package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.v1.post.LoginDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.exception.InvalidUsernameOrPasswordException;
import br.com.elysian.tartarus.exception.ReCaptchaInvalidException;
import br.com.elysian.tartarus.exception.ReCaptchaUnavailableException;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.captcha.CaptchaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Log in and be authorized")
public class AuthController {


    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final CaptchaService captchaService;
    private final AuthService authService;

    @Autowired
    public AuthController(CaptchaService captchaService, AuthService authService) {
        this.captchaService = captchaService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(
            @RequestParam(value = "longLivedToken", required = false) boolean longLivedToken,
            @Valid @RequestBody LoginDTO auth,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ReCaptchaUnavailableException, ReCaptchaInvalidException, InvalidUsernameOrPasswordException {
        if (!activeProfile.equals("dev")) {
            String captchaResponse = request.getHeader("g-recaptcha-response");
            captchaService.processResponse(captchaResponse);
        }

        TokenDTO token = authService.getAuthorization(auth, longLivedToken);
        authService.setHeaders(token, response);
        return ResponseEntity.ok(token);
    }

}
