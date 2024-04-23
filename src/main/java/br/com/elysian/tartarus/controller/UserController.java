package br.com.elysian.tartarus.controller;

import br.com.elysian.tartarus.annotation.SecurityXSS;
import br.com.elysian.tartarus.dto.AuthDTO;
import br.com.elysian.tartarus.dto.RegistrationDTO;
import br.com.elysian.tartarus.exception.ReCaptchaException;
import br.com.elysian.tartarus.model.User;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.UserService;
import br.com.elysian.tartarus.service.captcha.CaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserService userService;


    @SecurityXSS
    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@Valid @RequestBody RegistrationDTO registrationDTO, HttpServletRequest request) throws ReCaptchaException {
        String captchaResponse = request.getHeader("g-recaptcha-response");
        captchaService.processResponse(captchaResponse);

        String token = userService.saveUser(registrationDTO)
                .getAuthorization();

        return ResponseEntity.ok(token);
    }

}
