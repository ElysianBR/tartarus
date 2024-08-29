package br.com.elysian.tartarus.exception;


public class AccountNotRegisteredInCompanyNetworkException extends Exception {

    public AccountNotRegisteredInCompanyNetworkException() {
    }

    public AccountNotRegisteredInCompanyNetworkException(String message) {
        super(message);
    }

    public AccountNotRegisteredInCompanyNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotRegisteredInCompanyNetworkException(Throwable cause) {
        super(cause);
    }

    public AccountNotRegisteredInCompanyNetworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
