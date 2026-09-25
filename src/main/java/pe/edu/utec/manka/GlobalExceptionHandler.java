package pe.edu.utec.manka;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.utec.manka.exception.BusinessRuleException;
import pe.edu.utec.manka.exception.ConflictException;
import pe.edu.utec.manka.exception.InvalidTokenException;
import pe.edu.utec.manka.exception.InvalidCredentialsException;
import pe.edu.utec.manka.exception.ResourceNotFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail invalidCredentialsHandler(InvalidCredentialsException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problemDetail.setTitle("Unauthorized");
        problemDetail.setDetail(ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail invalidTokenHandler(InvalidTokenException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problemDetail.setTitle("Unauthorized");
        problemDetail.setDetail(ex.getMessage());

        return problemDetail;
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail accessDeniedHandler(AccessDeniedException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        problemDetail.setTitle("Forbidden");
        problemDetail.setDetail("You do not have permission to access this resource");

        return problemDetail;
    }


    @ExceptionHandler(ConflictException.class)
    public ProblemDetail conflictHandler(ConflictException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Conflict");
        problemDetail.setDetail(ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail notFoundHandler(ResourceNotFoundException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Resource Not Found");
        problemDetail.setDetail(ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail businessRuleHandler(BusinessRuleException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setDetail(ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validationHandler(
            MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Validation Error");

        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail unreadableHandler(
            HttpMessageNotReadableException ex) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Invalid Request Body");
        problemDetail.setDetail("The request body is invalid or malformed");

        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail dataIntegrityHandler(
            DataIntegrityViolationException ex) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Conflict");
        problemDetail.setDetail("Resource already exists");

        return problemDetail;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail genericHandler(RuntimeException ex) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problemDetail.setTitle("Server Error");
        problemDetail.setDetail("Unexpected server error");

        return problemDetail;
    }
}