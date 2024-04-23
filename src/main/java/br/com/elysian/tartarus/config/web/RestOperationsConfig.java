package br.com.elysian.tartarus.config.web;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
public class RestOperationsConfig {

    @Bean
    public RestOperations restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
