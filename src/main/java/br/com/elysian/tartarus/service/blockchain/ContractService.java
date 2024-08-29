package br.com.elysian.tartarus.service.blockchain;


import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.model.*;
import br.com.elysian.tartarus.model.pk.CompanyNetworkContractPK;
import br.com.elysian.tartarus.model.pk.ContractPK;
import br.com.elysian.tartarus.repository.ContractDAO;
import br.com.elysian.tartarus.repository.NetworkContractDAO;
import br.com.elysian.tartarus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ContractService {

    private final ContractDAO contractDAO;
    private final NetworkContractDAO networkContractDAO;
    private final NetworkService networkService;
    private final MessageService messageService;


    @Autowired
    public ContractService(ContractDAO contractDAO, NetworkContractDAO networkContractDAO, NetworkService networkService, MessageService messageService) {
        this.contractDAO = contractDAO;
        this.networkContractDAO = networkContractDAO;
        this.networkService = networkService;
        this.messageService = messageService;
    }


    public CompanyNetworkContract getNetworkContract(String contractName, String version) throws ContractNotFoundException, NetworkNotLoggedInException, CompanyNotLoggedInException, NetworkNotFoundException, NetworkContractNotRegistered {
        ContractPK contractPK = new ContractPK(contractName, version);
        Optional<Contract> contractOpt = contractDAO.findById(contractPK);

        if (contractOpt.isPresent()) {

            Contract contract = contractOpt.get();

            CompanyNetwork companyNetwork = networkService.getCompanyNetworkLoggedIn();

            //            CompanyNetworkPK companyNetworkPK = new CompanyNetworkPK(companyNetwork.getCompany(), companyNetwork.getNetwork());
            //            CompanyNetworkContractPK companyNetworkContractPK = new CompanyNetworkContractPK(companyNetworkPK, contractPK);

            CompanyNetworkContractPK companyNetworkContractPK = new CompanyNetworkContractPK(companyNetwork, contract);

            Optional<CompanyNetworkContract> companyNetworkContract = networkContractDAO.findById(companyNetworkContractPK);
            if (companyNetworkContract.isPresent()) {
                return companyNetworkContract.get();
            }


            throw new NetworkContractNotRegistered(String.format(messageService.getMessage("tartarus.blockchain.contract.not-registered-in-company"), contractName, version,  companyNetwork.getNetwork().getNetworkNameFormated()));
        }

        throw new ContractNotFoundException(String.format(messageService.getMessage("tartarus.blockchain.contract.not-found"), contractName, version));
    }

}
