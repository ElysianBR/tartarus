package br.com.elysian.tartarus.exception;


public class PrivateKeyNotSet extends Exception {

    public PrivateKeyNotSet() {
    }

    public PrivateKeyNotSet(String message) {
        super(message);
    }

    public PrivateKeyNotSet(String message, Throwable cause) {
        super(message, cause);
    }

    public PrivateKeyNotSet(Throwable cause) {
        super(cause);
    }

    public PrivateKeyNotSet(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
