package br.com.elysian.tartarus.config.web;


import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class JacksonViewInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    public JacksonViewInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // Register Views Role
        AuthenticationAccount auth = AuthService.getCurrentAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String role = auth.getSelectedGranted().getAuthority();

            // Configurar o ObjectMapper para a view correta
            objectMapper.setConfig(objectMapper.getSerializationConfig().withView(RoleViews.get(role)));
        }

        return true;
    }
}
