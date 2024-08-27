package br.com.elysian.tartarus.exception;


public class NetworkNotLoggedInException extends Exception {

    public NetworkNotLoggedInException() {
    }

    public NetworkNotLoggedInException(String message) {
        super(message);
    }

    public NetworkNotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetworkNotLoggedInException(Throwable cause) {
        super(cause);
    }

    public NetworkNotLoggedInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
