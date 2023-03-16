package com.hr.app.jpaexample.i18n.exceptions.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * ApiError represents the api error details.
 * @author ravibeli@gmail.com
 * @project jpa-example
 *
 **/

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "code",
    "message",
    "debugMessage",
    "timestamp"
})
public class ApiError {
    private String code;
    private HttpStatus status;

    private HttpStatusCode statusCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatusCode statusCode) {
        this();
        this.statusCode = statusCode;
    }

    /**
     * Parameterized constructor.
     * @param status the http status
     * @param message the error message
     * @param ex the exception object
     */
    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    /**
     * Parameterized constructor.
     * @param status the http status
     * @param code the error code
     * @param message the error message
     * @param ex the exception object
     */
    public ApiError(HttpStatus status, String code, String message, Throwable ex) {
        this();
        this.code = code;
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
