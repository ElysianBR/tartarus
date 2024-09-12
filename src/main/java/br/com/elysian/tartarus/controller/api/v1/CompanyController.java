package br.com.elysian.tartarus.controller.api.v1;

import br.com.elysian.tartarus.dto.v1.CompanyDTO;
import br.com.elysian.tartarus.dto.v1.PutCompanyDTO;
import br.com.elysian.tartarus.dto.v1.TokenDTO;
import br.com.elysian.tartarus.exception.CompanyNotFoundException;
import br.com.elysian.tartarus.exception.CompanyNotLoggedInException;
import br.com.elysian.tartarus.exception.InvalidUsernameOrPasswordException;
import br.com.elysian.tartarus.model.Company;
import br.com.elysian.tartarus.service.AuthService;
import br.com.elysian.tartarus.service.CompanyService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {


    private final CompanyService companyService;
    private final AuthService authService;


    public CompanyController(CompanyService companyService, AuthService authService) {
        this.companyService = companyService;
        this.authService = authService;
    }


    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<Company> get() throws CompanyNotLoggedInException {
        return ResponseEntity.ok(
                companyService.getCompanyLoggedIn()
        );
    }


    @PutMapping()
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<Company> put(@Valid @RequestBody PutCompanyDTO companyDTO) {
        return ResponseEntity.ok(
                companyService.put(companyDTO)
        );
    }


//    @PatchMapping()
//    @PreAuthorize("hasAnyAuthority('BUSINESS_ADMIN')")
//    public ResponseEntity<CompanyDTO> patch(@Valid @RequestBody SaveCompanyDTO companyDTO) throws CompanyNotLoggedInException {
//        return ResponseEntity.ok(
//                new CompanyDTO(
//                        companyService.update(companyDTO)
//                )
//        );
//    }


    @PostMapping("/with")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<TokenDTO> with(@RequestParam("companyId") Long companyId, HttpServletResponse response) throws CompanyNotFoundException, InvalidUsernameOrPasswordException {
        companyService.with(companyId);

        TokenDTO token = authService.getAuthorizationByCurrentAuthentication();
        authService.setHeaders(token, response);

        return ResponseEntity.ok(token);
    }


    @GetMapping("/companies")
    @PreAuthorize("hasAnyAuthority('ACCOUNT')")
    public ResponseEntity<ArrayList<CompanyDTO>> getCompanies() {
        Set<Company> companySet = companyService.getCompaniesFromAccount();

        ArrayList<CompanyDTO> companyDTOS = new ArrayList<>();
        companySet.forEach(company -> {
            if (company.isActive()) {
                companyDTOS.add(new CompanyDTO(company));
            }
        });

        return ResponseEntity.ok(companyDTOS);
    }


}
