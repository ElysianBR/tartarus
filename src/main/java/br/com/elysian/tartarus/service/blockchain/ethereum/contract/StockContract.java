package br.com.elysian.tartarus.service.blockchain.ethereum.contract;

import br.com.elysian.tartarus.service.blockchain.ethereum.ContractInstantiationFailed;
import br.com.elysian.tartarus.service.blockchain.ethereum.client.EtherClient;
import br.com.elysian.tartarus.service.blockchain.ethereum.model.contract.stock.Stock;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class StockContract extends ContractImp<Stock> {

    private static final String CONTRACT_ADRESS = "0x93e2c01BB03d9803DAFF0562aAfbF1bf43ceF35d";
    private static final BigInteger GAS_PRICE = Convert.toWei(BigDecimal.valueOf(50), Convert.Unit.GWEI).toBigInteger();

    private final Stock stock;

    public StockContract(EtherClient client) throws ContractInstantiationFailed {
        super(Stock.class, CONTRACT_ADRESS, client.getWeb3j(), client.getClientAdress(), GAS_PRICE, BigInteger.valueOf(Long.MAX_VALUE));

        this.stock = super.load();
    }

    public StockContract(EtherClient client, BigInteger gasLimit) throws ContractInstantiationFailed {
        super(Stock.class, CONTRACT_ADRESS, client.getWeb3j(), client.getClientAdress(), GAS_PRICE, gasLimit);

        this.stock = super.load();
    }

}
