package br.com.elysian.tartarus.model.pk;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ContractPK implements Serializable {
    private String name;
    private String version;
}