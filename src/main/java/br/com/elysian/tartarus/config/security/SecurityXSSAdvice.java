package br.com.elysian.tartarus.config.security;

import br.com.elysian.tartarus.annotation.SecurityXSS;
import jakarta.servlet.http.HttpServletRequest;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

@ControllerAdvice
public class SecurityXSSAdvice implements ResponseBodyAdvice<Object> {

    Logger logger = LoggerFactory.getLogger(SecurityXSSAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Method method = returnType.getMethod();

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String xssSecurity = httpServletRequest.getHeader("X-XSS-Security");

        return method != null && (method.isAnnotationPresent(SecurityXSS.class) || (xssSecurity != null && xssSecurity.equals("true")));
    }


    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {


        if (body != null) {
            Field[] bodyFields = body.getClass().getDeclaredFields();

            for (Field field : bodyFields) {
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(body);
                    if (fieldValue instanceof String) {
                        field.set(body, Encode.forHtml((String) fieldValue));
                    }
                } catch (IllegalAccessException e) {
                    logger.warn("IllegalAccessException in HTML encode.", e);
                }
            }
        }


        return body;
    }
}
