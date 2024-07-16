package br.com.elysian.tartarus.dto.v1;

import br.com.elysian.tartarus.model.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyDTO {

    private long id;
    private boolean active;
    private String cnpj;
    private String companyName;

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.active = company.isActive();
        this.cnpj = company.getCnpj();
        this.companyName = company.getCompanyName();
    }


}
