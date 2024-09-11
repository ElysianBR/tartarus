package br.com.elysian.tartarus.service.blockchain;


import br.com.elysian.tartarus.dto.v1.blockchain.ContractDeployDTO;
import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.repository.CompanyNetworkContractDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;


@Service
public class SupplyChainService {

    private final ContractService contractService;
    private final NetworkService networkService;

    private final CompanyNetworkContractDAO companyNetworkContractDAO;


    @Autowired
    public SupplyChainService(ContractService contractService, NetworkService networkService, CompanyNetworkContractDAO companyNetworkContractDAO) {
        this.contractService = contractService;
        this.networkService = networkService;
        this.companyNetworkContractDAO = companyNetworkContractDAO;
    }



    public <T extends org.web3j.tx.Contract> ContractDeployDTO saveDeployInDB(T deployOut, String contractName, String version) throws NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, ContractNotFoundException {
        Contract contract = contractService.getContract(contractName, version);
        CompanyNetwork companyNetwork = networkService.getCompanyNetworkLoggedIn();
        String contractAddress = deployOut.getContractAddress();

        CompanyNetworkContract companyNetworkContract = new CompanyNetworkContract(companyNetwork, contract, BigInteger.ZERO, contractAddress);
        return new ContractDeployDTO(
                companyNetworkContractDAO.save(companyNetworkContract)
        );
    }

}