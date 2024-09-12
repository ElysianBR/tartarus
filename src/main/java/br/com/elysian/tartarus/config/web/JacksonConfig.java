package br.com.elysian.tartarus.config.web;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import java.text.SimpleDateFormat;

@Configuration
public class JacksonConfig {


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register Simples Date format
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));



        // Register Views Role
        AuthenticationAccount auth = AuthService.getCurrentAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String role = auth.getSelectedGranted().getAuthority();
            objectMapper.setConfig(objectMapper.getSerializationConfig().withView(RoleViews.get(role)));
        }

        return objectMapper;
    }
}
