package br.com.elysian.tartarus.config.web.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private String error;
    private String message;

    public ErrorResponse(Exception error) {
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.name();
        this.message = error.getMessage();
    }

    public ErrorResponse(int statusCode, Exception error) {
        HttpStatus status = HttpStatus.resolve(statusCode);
        if (status != null) {
            this.statusCode = statusCode;
            this.error = status.name();
        } else {
            this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            this.error = HttpStatus.INTERNAL_SERVER_ERROR.name();
        }
        this.message = error.getMessage();
    }

    public ErrorResponse(HttpStatus status, Exception error) {
        this.statusCode = status.value();
        this.error = status.name();
        this.message = error.getMessage();
    }

    public ErrorResponse(String message) {
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.name();
        this.message = message;
    }

    public ErrorResponse(int statusCode, String message) {
        HttpStatus status = HttpStatus.resolve(statusCode);
        if (status != null) {
            this.statusCode = statusCode;
            this.error = status.name();
        } else {
            this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            this.error = HttpStatus.INTERNAL_SERVER_ERROR.name();
        }
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message) {
        this.statusCode = status.value();
        this.error = status.name();
        this.message = message;
    }
}