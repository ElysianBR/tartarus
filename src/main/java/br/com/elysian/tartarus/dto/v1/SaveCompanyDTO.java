package br.com.elysian.tartarus.dto.v1;

import br.com.elysian.tartarus.model.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveCompanyDTO {

    private String cnpj;
    private String companyName;

    public SaveCompanyDTO(Company company) {
        this.cnpj = company.getCnpj();
        this.companyName = company.getCompanyName();
    }


}
