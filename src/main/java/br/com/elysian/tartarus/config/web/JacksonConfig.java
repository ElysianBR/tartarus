package br.com.elysian.tartarus.config.web;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.view.RoleViews;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.text.SimpleDateFormat;

@Configuration
public class JacksonConfig {


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Register Simples Date format
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"));

        return mapper;
    }


//    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//        return new Jackson2ObjectMapperBuilder()
//                .defaultViewInclusion(true);
//    }

}