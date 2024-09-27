package br.com.elysian.tartarus.config.documentation;

import br.com.elysian.tartarus.dto.v1.blockchain.ExecuteRemoteDTO;
import br.com.elysian.tartarus.dto.v1.blockchain.ParameterDTO;
import br.com.elysian.tartarus.model.CompanyNetworkContract;
import br.com.elysian.tartarus.model.Contract;
import br.com.elysian.tartarus.service.blockchain.ContractService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.*;

@Component
public class CustomOpenApiCustomizer implements OpenApiCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomOpenApiCustomizer.class);

    private final ContractService contractService;

    @Autowired
    public CustomOpenApiCustomizer(ContractService contractService) {
        this.contractService = contractService;
    }


    @Override
    public void customise(OpenAPI openApi) {
        this.contractsCustom(openApi);
    }


    private void contractsCustom(OpenAPI openApi) {

        try {
            Iterable<Contract> contracts = contractService.getContractsAvailable();
            ObjectMapper mapper = new ObjectMapper();


            for (Contract contract : contracts) {
                String abiStr = contract.getAbi();

                if (abiStr != null) {
                    LOGGER.debug("Contract: {}", contract.getName());

                    List<Map<String, Object>> abi = mapper.readValue(abiStr, List.class);

                    for (Map<String, Object> objAbi : abi) {

                        // TODO: Quando os contratos tiverem a divisão do módulo completa, utilizar o módulo do contrato, não o fixo.
                        contract(openApi, "SupplyChain", contract);
                        metadata(openApi, "SupplyChain", contract);
                        deploy(openApi, "SupplyChain", contract, objAbi);
                        functions(openApi, "SupplyChain", contract, objAbi);

                    }

                }
            }
        } catch (Exception e) {
            LOGGER.error("Swagger Dynamic Contracts Error", e);
        }

    }


    private void contract(OpenAPI openApi, String module, Contract contract) {
        openApi.path(
                String.format("/api/v1/blockchain/%s/%s/%s", module, contract.getVersion(), contract.getName()),
                new PathItem().post(
                        generateOperation(
                                module + "-" + contract.getVersion() + "-" + contract.getName(),
                                null,
                                CompanyNetworkContract.class
                        )
                )
        );
    }


    private void metadata(OpenAPI openApi, String module, Contract contract) {
        openApi.path(
                String.format("/api/v1/blockchain/%s/%s/%s/metadata", module, contract.getVersion(), contract.getName()),
                new PathItem().post(
                        generateOperation(
                                module + "-" + contract.getVersion() + "-" + contract.getName(),
                                null,
                                Contract.class
                        )
                )
        );
    }


    private void deploy(OpenAPI openApi, String module, Contract contract, Map<String, Object> objAbi) {
        if ("constructor".equals(objAbi.get("type"))) {
            List<Map<String, Object>> inputs = (List<Map<String, Object>>) objAbi.get("inputs");
            openApi.path(
                    String.format("/api/v1/blockchain/%s/%s/%s/deploy", module, contract.getVersion(), contract.getName()),
                    new PathItem().post(
                            generateOperation(
                                    module + "-" + contract.getVersion() + "-" + contract.getName(),
                                    generateRequestBody(inputs),
                                    CompanyNetworkContract.class
                            )
                    )
            );
        }
    }


    private void functions(OpenAPI openApi, String module, Contract contract, Map<String, Object> objAbi) {
        if ("function".equals(objAbi.get("type"))) {
            String functionName = (String) objAbi.get("name");

            List<Map<String, Object>> inputs = (List<Map<String, Object>>) objAbi.get("inputs");


            openApi.path(
                    String.format("/api/v1/blockchain/%s/%s/%s/%s", module, contract.getVersion(), contract.getName(), functionName),
                    new PathItem().post(
                            generateOperation(
                                    module + "-" + contract.getVersion() + "-" + contract.getName(),
                                    generateRequestBody(inputs),
                                    TransactionReceipt.class
                            )
                    )
            );
        }
    }


    private RequestBody generateRequestBody(List<Map<String, Object>> inputs) {
        Schema executeRemote = generateSchemaFromClass(ExecuteRemoteDTO.class);

        Schema<Map<String, ParameterDTO>> parametersMapSchema = new Schema<>();
        for (Map<String, Object> input : inputs) {
            String name = (String) input.get("name");
            String type = (String) input.get("type");

            Schema<ParameterDTO> parameterDTOSchema = new Schema<>();
            parameterDTOSchema.addProperty("type", new Schema<>().type("string").example(type));
            parameterDTOSchema.addProperty("value", new Schema<>().type("object"));

            parametersMapSchema.addProperty(name, parameterDTOSchema);
        }

        executeRemote.addProperty("parameters", parametersMapSchema);

        // Criar um RequestBody dinâmico
        return new RequestBody()
                .content(new Content()
                        .addMediaType("application/json", new MediaType().schema(executeRemote))
                );
    }


    private static Operation generateOperation(String tag, RequestBody requestBody, Class<?> response) {
        return new Operation()
                .requestBody(requestBody)
                .responses(new ApiResponses()
                        .addApiResponse(
                                "200",
                                new ApiResponse()
                                        .description("OK")
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(generateSchemaFromClass(response))
                                                )
                                        )
                        )
                )
                .tags(Collections.singletonList(tag));
    }

    private static Schema<?> generateSchemaFromClass(Class<?> clazz) {
        // Usa o ModelConverters para converter a classe Java para um Schema
        return ModelConverters.getInstance()
                .read(clazz)
                .values()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
