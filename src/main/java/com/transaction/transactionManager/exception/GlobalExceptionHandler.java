package com.transaction.transactionManager.exception;

import com.transaction.transactionManager.payload.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Divakar Verma
 * @created_at : 31/01/2024 - 1:10 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse> handleInvalidTokenException(InvalidTokenException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServerErrorException.class)
    public ResponseEntity<ApiResponse> handleServerErrorException(ServerErrorException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConnectionRefusedException.class)
    public ResponseEntity<ApiResponse> handleConnectionRefusedException(ConnectionRefusedException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse> handleGlobalException(GlobalException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ValidationErrorException.class)
    public ResponseEntity<ApiResponse> handleValidationErrorException(ValidationErrorException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> violation : constraintViolations) {
            errorMessage.append(violation.getMessage()).append("; ");
        }
        ApiResponse apiResponse = new ApiResponse(errorMessage.toString(),false);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientWalletBalanceException.class)
    public ResponseEntity<ApiResponse> handleInsufficientWalletBalanceException(InsufficientWalletBalanceException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
