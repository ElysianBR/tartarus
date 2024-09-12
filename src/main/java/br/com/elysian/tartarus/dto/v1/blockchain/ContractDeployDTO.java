package br.com.elysian.tartarus.dto.v1.blockchain;


import br.com.elysian.tartarus.model.CompanyNetworkContract;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@AllArgsConstructor
public class ContractDeployDTO {

    private Long companyId;
    private Long networkId;
    private String contractName;
    private String version;
    private BigInteger gasPriceWei;
    private BigInteger maxGasWei;
    private String contractAddress;

    public ContractDeployDTO(CompanyNetworkContract companyNetworkContract) {
        companyId = companyNetworkContract.getPk().getCompanyNetwork().getCompany().getId();
        networkId = companyNetworkContract.getPk().getCompanyNetwork().getNetwork().getId();
        contractName = companyNetworkContract.getPk().getContract().getName();
        version = companyNetworkContract.getPk().getContract().getVersion();
        maxGasWei = companyNetworkContract.getMaxGasWei();
        contractAddress = companyNetworkContract.getContractAddress();
    }

}
