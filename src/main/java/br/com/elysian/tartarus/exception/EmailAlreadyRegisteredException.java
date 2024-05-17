package br.com.elysian.tartarus.exception;

public class EmailAlreadyRegisteredException extends Exception {


    public EmailAlreadyRegisteredException() {
    }

    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }

    public EmailAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

    public EmailAlreadyRegisteredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
