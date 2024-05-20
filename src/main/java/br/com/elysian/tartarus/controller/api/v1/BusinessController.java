package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.BusinessDTO;
import br.com.elysian.tartarus.model.Account;
import br.com.elysian.tartarus.model.Business;
import br.com.elysian.tartarus.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Transactional
@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService){
        this.businessService = businessService;
    }


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ACCOUNT')")
    @GetMapping("/registeredBusiness")
    public ResponseEntity<ArrayList<BusinessDTO>> registeredBusiness(@Param("activeOnly") boolean activeOnly) {
        return ResponseEntity.ok(businessService.getBusinessFromAccountLogged(activeOnly));
    }

}
