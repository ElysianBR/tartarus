package br.com.elysian.tartarus.config.error;

import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final MessageService messageService;

    @Autowired
    public GlobalExceptionHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    private ResponseEntity<ErrorResponse> getResponseEntity(HttpStatusCode status, Exception e) {
        LOGGER.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(status.value(), e);
        return ResponseEntity.status(status).body(errorResponse);
    }


    private ResponseEntity<ErrorResponse> getResponseEntity(HttpStatusCode status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        return ResponseEntity.status(status).body(errorResponse);
    }


    @ExceptionHandler({NoResourceFoundException.class, BlockchainNotFoundException.class, CompanyNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e) {
        return getResponseEntity(HttpStatus.NOT_FOUND, e);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValid(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage(), e);
        
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message;

        if (fieldError != null) {
            message = String.format("( %s ) %s", fieldError.getField(), fieldError.getDefaultMessage());
        } else {
            message = this.messageService.getMessage("tartarus.validations.argument-not-valid");
        }

        return getResponseEntity(HttpStatus.BAD_REQUEST, message);
    }


    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException e) {
        return getResponseEntity(HttpStatus.CONFLICT, e);
    }


    @ExceptionHandler({InvalidUsernameOrPasswordException.class, MismatchedPasswordsException.class, BadCredentialsException.class, NetworkNotLoggedInException.class, CompanyNotLoggedInException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(Exception e) {
        return getResponseEntity(HttpStatus.UNAUTHORIZED, e);
    }


    @ExceptionHandler({ReCaptchaException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(Exception e) {
        return getResponseEntity(HttpStatus.FORBIDDEN, e);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
