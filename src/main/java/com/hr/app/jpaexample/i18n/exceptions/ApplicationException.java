package com.hr.app.jpaexample.i18n.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@JsonRootName("error")
@Getter
@Setter
public class ApplicationException extends RuntimeException {
    private String message;
    private String causeMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    @JsonIgnore
    private HttpStatus httpStatus;
    private String code;
    @JsonIgnore
    private transient Object[] arguments;

    /**
     * Default constructor.
     */
    public ApplicationException(String message) {
        super(message);
    }

    /**
     * ApplicationException constructor.
     *
     * @param message    the message
     * @param cause      the cause
     * @param httpStatus the httpStatus
     * @param code       the error code
     * @param arguments  other arguments
     */
    public ApplicationException(String message, Throwable cause, HttpStatus httpStatus, String code,
                                Object... arguments) {
        super(cause);
        this.message = message;
        this.httpStatus = httpStatus;
        this.code = code;
        this.timestamp = LocalDateTime.now();
        this.arguments = arguments;
        this.causeMessage = cause == null ? null : cause.getMessage();
    }

    /**
     * ApplicationException parameterized constructor.
     *
     * @param message    the message
     * @param httpStatus the httpStatus
     * @param code       the code
     * @param arguments  the arguments
     */
    public ApplicationException(String message, HttpStatus httpStatus, String code, Object... arguments) {
        this(message, (Throwable) null, httpStatus, code, arguments);
    }


    /**
     * ApplicationException parameterized constructor.
     *
     * @param httpStatus the httpStatus
     * @param code       the code
     * @param arguments  the arguments
     */
    public ApplicationException(HttpStatus httpStatus, String code, Object... arguments) {
        this((String) null, (Throwable) null, httpStatus, code, arguments);
    }


    /**
     * ApplicationException parameterized constructor.
     *
     * @param httpStatus the httpStatus
     * @param code       the code
     * @param arguments  the arguments
     */
    public ApplicationException(Throwable cause, HttpStatus httpStatus, String code, Object... arguments) {
        this((String) null, cause, httpStatus, code, arguments);
    }
}