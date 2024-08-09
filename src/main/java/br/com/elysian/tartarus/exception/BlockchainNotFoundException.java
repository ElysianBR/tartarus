package br.com.elysian.tartarus.exception;


public class BlockchainNotFoundException extends Exception {

    public BlockchainNotFoundException() {
    }

    public BlockchainNotFoundException(String message) {
        super(message);
    }

    public BlockchainNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockchainNotFoundException(Throwable cause) {
        super(cause);
    }

    public BlockchainNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
