package com.hr.app.jpaexample.i18n.exceptions.handler;

import com.hr.app.jpaexample.i18n.exceptions.ApplicationException;
import com.hr.app.jpaexample.i18n.exceptions.dto.ApiError;
import com.hr.app.jpaexample.i18n.exceptions.dto.ValidationErrorResponse;
import com.hr.app.jpaexample.i18n.exceptions.dto.Violation;
import com.hr.app.jpaexample.i18n.exceptions.helper.LocalizationTranslator;
import jakarta.persistence.EntityNotFoundException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


/**
 * Global exception handler for rest API.
 *
 * @author ravibeli@gmail.com
 * @project jpa-example
 **/

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String TRACE = "trace";

    @Autowired
    LocalizationTranslator localizationTranslator;

    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    public boolean isPrintStackTrace() {
        return printStackTrace;
    }

    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<Object> handleApplicationException(ApplicationException ex) {
        log.error("Application exception occurred", ex);
        ApplicationException exception = localizationTranslator.localize(ex);
        ApiError apiError = new ApiError(exception.getHttpStatus());
        apiError.setCode(exception.getCode());
        apiError.setMessage(exception.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle all uncaught exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(Exception exception, WebRequest request) {
        log.error("Unhandled exception occurred", exception);
        return buildErrorResponse(exception, exception.getMessage(), INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("Invalid input provided", ex);
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        log.error("Error response from SCPO API", ex);
        ApiError apiError = new ApiError(ex.getStatusCode());
        apiError.setMessage(ex.getResponseBodyAsString());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid input provided", ex);
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("Entity not found exception", ex);
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    /**
     * Handle BatchUpdateException exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return
     */
    @ExceptionHandler(BatchUpdateException.class)
    public ResponseEntity<ApiError> handleBatchUpdateException(Exception exception, WebRequest request) {
        log.error("SQL BatchUpdateException: ", exception);
        return buildErrorResponse(exception, exception.getMessage(), INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handle SQLSyntaxErrorException exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return
     */
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public ResponseEntity<ApiError> handleSqlSyntaxErrorException(Exception exception, WebRequest request) {
        log.error("SQL Syntax Error: ", exception);
        return buildErrorResponse(exception, exception.getMessage(), INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handle SQLException exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiError> handleSqlExceptionHelper(Exception exception, WebRequest request) {
        log.error("SqlExceptionHelper: ", exception);
        return buildErrorResponse(exception, exception.getMessage(), INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handle Database Constraint Violation exception.
     *
     * @param exception the exception
     * @param request   the request
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException exception,
                                                                          WebRequest request) {
        log.error("SQL Constraint Violation Error: ", exception);
        return buildErrorResponse(exception, exception.getMessage(), BAD_REQUEST, request);
    }

    /**
     * handleHttpMessageNotReadable converted technical error to readable message.
     *
     * @param ex      the exception
     * @param headers the headers
     * @param statusCode  the statusCode
     * @param request the request
     * @return response
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               HttpHeaders headers, HttpStatusCode statusCode,
                                                               WebRequest request) {
        var apiError = new ApiError(BAD_REQUEST, "Invalid input", ex);
        if (isTraceOn(request) && printStackTrace) {
            apiError.setDebugMessage(ExceptionUtils.getStackTrace(ex));
        }
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ResponseEntity<ApiError> buildErrorResponse(Exception exception, String message, HttpStatus status,
                                                        WebRequest request) {
        var errorResponse = new ApiError(status, message, exception);

        if (isTraceOn(request) && printStackTrace) {
            errorResponse.setDebugMessage(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(status).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode statusCode,
                                                                  WebRequest request) {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError ->
                        validationErrorResponse.getViolations()
                                .add(new Violation(fieldError.getField(), fieldError.getDefaultMessage())));

        return new ResponseEntity<>(validationErrorResponse, statusCode);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }


}
