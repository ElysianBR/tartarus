package br.com.elysian.tartarus.service.blockchain.ethereum.client;

import lombok.Getter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Getter
public abstract class EtherClient {

    private final String url;
    private final String clientAdress;
    private final Web3j web3j;


    protected EtherClient(String url, String clientAdress) {
        this.clientAdress = clientAdress;
        this.url = url;
        this.web3j = Web3j.build(new HttpService(this.url));
    }

}
