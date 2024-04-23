package br.com.elysian.tartarus.exception;

public class InvalidReCaptchaException extends ReCaptchaException {

    public InvalidReCaptchaException() {
    }

    public InvalidReCaptchaException(String message) {
        super(message);
    }

    public InvalidReCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReCaptchaException(Throwable cause) {
        super(cause);
    }

    public InvalidReCaptchaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
