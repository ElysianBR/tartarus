package br.com.elysian.tartarus.config.error;

import br.com.elysian.tartarus.exception.*;
import br.com.elysian.tartarus.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


    @ExceptionHandler({NoResourceFoundException.class, BlockchainNotFoundException.class, CompanyNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e) {
        LOGGER.error(e.getMessage(), e);
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyRegistered(EmailAlreadyRegisteredException e) {
        LOGGER.error(e.getMessage(), e);
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    @ExceptionHandler({InvalidUsernameOrPasswordException.class, MismatchedPasswordsException.class, BadCredentialsException.class, NetworkNotLoggedInException.class, CompanyNotLoggedInException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(Exception e) {
        LOGGER.error(e.getMessage(), e);
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    @ExceptionHandler({ReCaptchaException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(Exception e) {
        LOGGER.error(e.getMessage(), e);
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
