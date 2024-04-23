package br.com.elysian.tartarus.exception;
public class ReCaptchaUnavailableException extends ReCaptchaException {


    public ReCaptchaUnavailableException() {
    }

    public ReCaptchaUnavailableException(String message) {
        super(message);
    }

    public ReCaptchaUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaUnavailableException(Throwable cause) {
        super(cause);
    }

    public ReCaptchaUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
