package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.v1.post.LoginDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.exception.InvalidUsernameOrPasswordException;
import br.com.elysian.tartarus.exception.ReCaptchaInvalidException;
import br.com.elysian.tartarus.exception.ReCaptchaUnavailableException;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.captcha.CaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
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
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO auth, HttpServletRequest request, HttpServletResponse response) throws ReCaptchaUnavailableException, ReCaptchaInvalidException, InvalidUsernameOrPasswordException {
        if (!activeProfile.equals("dev")) {
            String captchaResponse = request.getHeader("g-recaptcha-response");
            captchaService.processResponse(captchaResponse);
        }

        TokenDTO token = authService.getAuthorization(auth);
        authService.setHeaders(token, response);
        return ResponseEntity.ok(token);
    }

}
