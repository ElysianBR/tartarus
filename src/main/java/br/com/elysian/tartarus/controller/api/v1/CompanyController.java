package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.v1.CompanyDTO;
import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.service.CompanyService;
import br.com.elysian.tartarus.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.Set;

@Transactional
@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;
    private final MessageService messageService;

    public CompanyController(CompanyService companyService, MessageService messageService) {
        this.companyService = companyService;
        this.messageService = messageService;
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ACCOUNT')")
    @GetMapping("/loggin")
    public ResponseEntity<String> loggin(@RequestParam("idComapany") Long idCompany) {
//        TODO: Implements Login Company, set JWT, Authorization e etc...
        return ResponseEntity.ok("");
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ACCOUNT')")
    @GetMapping("/getCompanies")
    public ResponseEntity<ArrayList<CompanyDTO>> getCompanies() {
        Set<Company> companySet = companyService.getBusinessFromAccount();

        ArrayList<CompanyDTO> companyDTOS = new ArrayList<>();
        companySet.forEach(business -> {
            if (business.isActive()) {
                companyDTOS.add(new CompanyDTO(business));
            }
        });

        return ResponseEntity.ok(companyDTOS);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ACCOUNT')")
    @GetMapping("/getCompany")
    public ResponseEntity<CompanyDTO> getCompany() throws CredentialException {
        Company company = companyService.getCompanyLoggedIn();

        if (company != null) {
            return ResponseEntity.ok(
                    new CompanyDTO(company)
            );
        }

        throw new CredentialException(messageService.getMessage("tartarus.authorization.company.no-logged-in"));
    }


}
