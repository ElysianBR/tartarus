package br.com.elysian.tartarus.exception;


public class ContractLoadFailed extends Exception {

    public ContractLoadFailed() {
    }

    public ContractLoadFailed(String message) {
        super(message);
    }

    public ContractLoadFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public ContractLoadFailed(Throwable cause) {
        super(cause);
    }

    public ContractLoadFailed(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
