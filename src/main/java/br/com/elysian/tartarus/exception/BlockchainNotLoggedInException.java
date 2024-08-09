package br.com.elysian.tartarus.exception;


public class BlockchainNotLoggedInException extends Exception {

    public BlockchainNotLoggedInException() {
    }

    public BlockchainNotLoggedInException(String message) {
        super(message);
    }

    public BlockchainNotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockchainNotLoggedInException(Throwable cause) {
        super(cause);
    }

    public BlockchainNotLoggedInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
