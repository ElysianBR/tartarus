package br.com.elysian.tartarus.exception;


public class CompanyNotLoggedInException extends Exception {

    public CompanyNotLoggedInException() {
    }

    public CompanyNotLoggedInException(String message) {
        super(message);
    }

    public CompanyNotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompanyNotLoggedInException(Throwable cause) {
        super(cause);
    }

    public CompanyNotLoggedInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
