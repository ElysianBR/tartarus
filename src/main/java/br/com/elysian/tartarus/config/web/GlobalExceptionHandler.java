package br.com.elysian.tartarus.config.web;

import br.com.elysian.tartarus.annotation.SecurityXSS;
import br.com.elysian.tartarus.exception.ReCaptchaException;
import br.com.elysian.tartarus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageService messageService;

    @Autowired
    public GlobalExceptionHandler(MessageService messageService) {
        this.messageService = messageService;
    }


    @SecurityXSS
    @ExceptionHandler({UsernameNotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @SecurityXSS
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message;

        if (fieldError != null) {
            message = String.format("( %s ) %s", fieldError.getField(), fieldError.getDefaultMessage());
        } else {
            message = this.messageService.getMessage("tartarus.argument.not.valid");
        }

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @SecurityXSS
    @ExceptionHandler(ReCaptchaException.class)
    public ResponseEntity<ErrorResponse> handleCaptchaException(ReCaptchaException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    @SecurityXSS
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
