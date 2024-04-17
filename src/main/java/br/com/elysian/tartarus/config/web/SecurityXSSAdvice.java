package br.com.elysian.tartarus.config.web;

import br.com.elysian.tartarus.annotation.SecurityXSS;
import br.com.elysian.tartarus.controller.response.ErrorResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.owasp.encoder.Encode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@ControllerAdvice
public class SecurityXSSAdvice implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Method method = returnType.getMethod();

        return method != null && method.isAnnotationPresent(SecurityXSS.class);
    }


    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {


        if (body instanceof String && (selectedContentType.includes(MediaType.TEXT_PLAIN) || selectedContentType.includes(MediaType.APPLICATION_JSON))) {

            return Encode.forHtml((String) body);

        }

        if (body instanceof ErrorResponse errorBody){

            errorBody.setMessage(Encode.forHtml(errorBody.getMessage()));
            errorBody.setErrro(Encode.forHtml(errorBody.getErrro()));

        }

        return body;
    }
}
