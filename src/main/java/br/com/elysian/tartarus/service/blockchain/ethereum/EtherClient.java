package br.com.elysian.tartarus.service.blockchain.ethereum;

import lombok.Getter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

@Getter
public class EtherClient {

    private final String url;
    private final String clientAdress;
    private final BigInteger maxGasWei;
    private final Web3j web3j;


    public EtherClient(String url, String clientAdress, BigInteger maxGasWei) {
        this.url = url;
        this.web3j = Web3j.build(new HttpService(this.url));

        this.clientAdress = clientAdress;
        this.maxGasWei = maxGasWei;
    }

}
