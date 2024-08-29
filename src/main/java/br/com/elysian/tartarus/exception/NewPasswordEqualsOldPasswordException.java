package br.com.elysian.tartarus.exception;


public class NewPasswordEqualsOldPasswordException extends Exception {

    public NewPasswordEqualsOldPasswordException() {
    }

    public NewPasswordEqualsOldPasswordException(String message) {
        super(message);
    }

    public NewPasswordEqualsOldPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewPasswordEqualsOldPasswordException(Throwable cause) {
        super(cause);
    }

    public NewPasswordEqualsOldPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
