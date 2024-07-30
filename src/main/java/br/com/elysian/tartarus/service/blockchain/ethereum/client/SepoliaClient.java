package br.com.elysian.tartarus.service.blockchain.ethereum.client;

import br.com.elysian.tartarus.service.AccountService;
import br.com.elysian.tartarus.service.CompanyService;
import br.com.elysian.tartarus.service.blockchain.BlockchainService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Getter
public class SepoliaClient extends EtherClient{

    private static final String URL = "https://rpc.sepolia.org";
    private final String clientAdress;

    public SepoliaClient(String clientAdress) {
        super(URL, clientAdress);
        this.clientAdress = clientAdress;
    }
}
