package br.com.elysian.tartarus.exception;


public class MismatchedPasswordsException extends Exception {

    public MismatchedPasswordsException() {
    }

    public MismatchedPasswordsException(String message) {
        super(message);
    }

    public MismatchedPasswordsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MismatchedPasswordsException(Throwable cause) {
        super(cause);
    }

    public MismatchedPasswordsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
