package br.com.elysian.tartarus.exception;

public class ReCaptchaException extends Exception {

    public ReCaptchaException() {
    }

    public ReCaptchaException(String message) {
        super(message);
    }

    public ReCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaException(Throwable cause) {
        super(cause);
    }

    public ReCaptchaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
