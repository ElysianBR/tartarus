package br.com.elysian.tartarus.service.captcha;

import br.com.elysian.tartarus.exception.ReCaptchaInvalidException;
import br.com.elysian.tartarus.exception.ReCaptchaUnavailableException;

public interface ICaptchaService {

    default void processResponse(final String response)  throws ReCaptchaInvalidException, ReCaptchaUnavailableException {}

    default void processResponse(final String response, String action) throws ReCaptchaInvalidException, ReCaptchaUnavailableException {}

    String getReCaptchaSite();

    String getReCaptchaSecret();
}