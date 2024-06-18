package br.com.elysian.tartarus.dto.v1;

import br.com.elysian.tartarus.model.Business;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessDTO {

    private boolean active;
    private String cnpj;
    private String companyName;

    public BusinessDTO(Business business) {
        this.active = business.isActive();
        this.cnpj = business.getCnpj();
        this.companyName = business.getCompanyName();
    }


}
