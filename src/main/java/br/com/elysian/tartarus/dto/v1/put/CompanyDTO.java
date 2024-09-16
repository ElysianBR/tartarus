package br.com.elysian.tartarus.dto.v1.put;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyDTO {

    private String cnpj;
    private String companyName;

}
