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
        companyId = companyNetworkContract.getCompanyNetwork().getCompany().getId();
        networkId = companyNetworkContract.getCompanyNetwork().getNetwork().getId();
        contractName = companyNetworkContract.getContract().getName();
        version = companyNetworkContract.getContract().getVersion();
        gasPriceWei = companyNetworkContract.getContract().getGasPriceWei();
        maxGasWei = companyNetworkContract.getMaxGasWei();
        contractAddress = companyNetworkContract.getContractAddress();
    }

}
