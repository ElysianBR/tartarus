package br.com.elysian.tartarus.config.documentation;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomOperationCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        generateGlobalRequestHeader(operation);
        return operation;
    }

    private void generateGlobalRequestHeader(Operation operation) {
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
