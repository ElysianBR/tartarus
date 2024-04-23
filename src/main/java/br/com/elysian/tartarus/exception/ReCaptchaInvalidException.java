package br.com.elysian.tartarus.exception;

public class ReCaptchaInvalidException extends ReCaptchaException {

    public ReCaptchaInvalidException() {
    }

    public ReCaptchaInvalidException(String message) {
        super(message);
    }

    public ReCaptchaInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaInvalidException(Throwable cause) {
        super(cause);
    }

    public ReCaptchaInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
