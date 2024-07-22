package br.com.elysian.tartarus.service.blockchain.ethereum.sepolia;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Service
@Getter
public class SepoliaClient {

    private static final String URL = "https://rpc.sepolia.org";
    private final Web3j web3j;

    public SepoliaClient() {
        web3j = Web3j.build(new HttpService(URL));
    }
}
