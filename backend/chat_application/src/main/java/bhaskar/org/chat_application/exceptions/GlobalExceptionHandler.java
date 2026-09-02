package bhaskar.org.chat_application.exceptions;

import bhaskar.org.chat_application.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentialException(InvalidCredentialException ex){
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailNotVerifiedException(EmailNotVerifiedException ex){
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyExistsException(AlreadyExistsException ex){
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(
                        error -> errors.put(error.getField(), error.getDefaultMessage())
                );

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
