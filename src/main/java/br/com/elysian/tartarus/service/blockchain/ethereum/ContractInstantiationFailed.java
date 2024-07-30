package br.com.elysian.tartarus.service.blockchain.ethereum;


public class ContractInstantiationFailed extends Exception {

    public ContractInstantiationFailed() {
    }

    public ContractInstantiationFailed(String message) {
        super(message);
    }

    public ContractInstantiationFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public ContractInstantiationFailed(Throwable cause) {
        super(cause);
    }

    public ContractInstantiationFailed(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
