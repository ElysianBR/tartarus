package br.com.elysian.tartarus.config.documentation;

import br.com.elysian.tartarus.annotation.SecurityXSS;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        generateGlobalPreAuthorize(operation, handlerMethod);
        generateGlobalRequestHeader(operation,handlerMethod);
        return operation;
    }


    private void generateGlobalRequestHeader(Operation operation, HandlerMethod handlerMethod) {
        SecurityXSS securityXSS = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), SecurityXSS.class);

        if (securityXSS == null) {
            List<Parameter> parameters = operation.getParameters();
            if (parameters == null) {
                parameters = new ArrayList<>();
            }
            var parameter = new Parameter();
            var schema = new Schema<>();
            schema.setType("boolean");
            parameter.setName("X-XSS-Security");
            parameter.setIn("header");
            parameter.setRequired(false);
            parameter.setDescription("Set this header to `true` for XSS encoding");
            parameter.setDeprecated(false);
            parameter.setSchema(schema);
            parameters.add(parameter);

            operation.setParameters(parameters);
        }
    }


    private void generateGlobalPreAuthorize(Operation operation, HandlerMethod handlerMethod) {
        PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), PreAuthorize.class);
        if (preAuthorize != null) {
            // TODO: Implementation for validation if PreAuthorize is Role or is Permission ou others and generate correct description.
            String regex = "\\('([^)]+)'\\)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(preAuthorize.value());


            ArrayList<String> permissions = new ArrayList<>();

            while (matcher.find()) {
                permissions.add(matcher.group(1));
            }

            if (!permissions.isEmpty()) {

                String authorizationInfo;
                if (permissions.size() > 1) {
                    authorizationInfo = String.format("<b>Roles required: <i>%s</i></b>", String.join(", ", permissions));
                } else {
                    authorizationInfo = String.format("<b>Role required: <i>%s</i></b>", permissions.getFirst());
                }

                operation.setDescription(operation.getDescription() != null
                        ? String.format("%s<br>%s", authorizationInfo, operation.getDescription())
                        : authorizationInfo);

            } else {
                operation.setDescription(operation.getDescription());
            }
        }
    }
}
