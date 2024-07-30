package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.config.security.authentication.AuthenticationAccount;
import br.com.elysian.tartarus.config.security.authentication.CredentialAccount;
import br.com.elysian.tartarus.dto.v1.CompanyDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.model.util.EntityInfo;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.CompanyService;
import br.com.elysian.tartarus.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.Set;

@Transactional
@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;
    private final MessageService messageService;
    private final AuthService authService;

    public CompanyController(CompanyService companyService, MessageService messageService, AuthService authService, EntityInfo entityInfo) {
        this.companyService = companyService;
        this.messageService = messageService;
        this.authService = authService;
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ACCOUNT')")
    @PostMapping("/swith")
    public ResponseEntity<TokenDTO> swith(@RequestParam("companyId") Long companyId, HttpServletResponse response) {
        // TODO: Implementar camada de seguranaça para fazer o set da credencial DENTREO do AuthService ou CompanyService, fazendo a validação se de fato o usuário possui acesso a emrpesa.
        AuthenticationAccount authenticationAccount = authService.getCurrentAuthentication();
        ((CredentialAccount) authenticationAccount.getCredentials()).setCompanyId(companyId);

        TokenDTO token = authService.getAuthorizationByCurrentAuthentication();
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
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
