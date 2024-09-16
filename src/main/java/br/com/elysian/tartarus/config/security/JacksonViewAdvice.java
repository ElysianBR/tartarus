package br.com.elysian.tartarus.config.security;


import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.view.RoleViews;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;


@ControllerAdvice
public class JacksonViewAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    @Override
    protected void beforeBodyWriteInternal(@NotNull MappingJacksonValue bodyContainer, @NotNull MediaType contentType, @NotNull MethodParameter returnType, @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {

        // TODO: ISSUE: https://github.com/ElysianBR/tartarus/issues/8

        String role = null;
        AuthenticationAccount auth = AuthService.getCurrentAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            GrantedAuthority granted = auth.getSelectedGranted();

            if (granted != null) {
                role = auth.getSelectedGranted().getAuthority();
            }
        }

        Class<?> jsonView = RoleViews.get(role);
        bodyContainer.setSerializationView(jsonView);

    }
}
