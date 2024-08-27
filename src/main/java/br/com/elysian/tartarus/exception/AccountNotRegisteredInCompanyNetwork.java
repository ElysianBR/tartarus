package br.com.elysian.tartarus.exception;


public class AccountNotRegisteredInCompanyNetwork extends Exception {

    public AccountNotRegisteredInCompanyNetwork() {
    }

    public AccountNotRegisteredInCompanyNetwork(String message) {
        super(message);
    }

    public AccountNotRegisteredInCompanyNetwork(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNotRegisteredInCompanyNetwork(Throwable cause) {
        super(cause);
    }

    public AccountNotRegisteredInCompanyNetwork(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
