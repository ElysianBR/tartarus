package br.com.elysian.tartarus.config.security;


import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.view.RoleViews;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;


@ControllerAdvice
public class JacksonViewAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = returnType.getMethod();

        return method != null && (method.isAnnotationPresent(PreAuthorize.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        AuthenticationAccount auth = AuthService.getCurrentAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            GrantedAuthority granted = auth.getSelectedGranted();

            if (granted != null) {
                String role = auth.getSelectedGranted().getAuthority();
                Class<?> jsonView = RoleViews.get(role);


                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(body);
                mappingJacksonValue.setSerializationView(jsonView);


                return mappingJacksonValue;
            }
        }


        return body;
    }
}
