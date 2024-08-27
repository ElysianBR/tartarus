package br.com.elysian.tartarus.exception;


public class NetworkContractNotRegistered extends Exception {

    public NetworkContractNotRegistered() {
    }

    public NetworkContractNotRegistered(String message) {
        super(message);
    }

    public NetworkContractNotRegistered(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkContractNotRegistered(Throwable cause) {
        super(cause);
    }

    public NetworkContractNotRegistered(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
