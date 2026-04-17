package com.example.ProjectDima3.exception;

import com.example.ProjectDima3.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import org.springframework.security.authorization.AuthorizationDeniedException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Обработка ошибок валидации (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Ошибка валидации: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                "Ошибка валидации данных",
                ex.getBindingResult().getFieldError().getDefaultMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); // Код 400
    }

    // Обработка всех остальных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Внутренняя ошибка сервера: ", ex);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                "Произошла непредвиденная ошибка",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR); // Код 500
    }

    //для тестирования
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.error("Доступ запрещен: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                "Доступ запрещен",
                "У вас нет прав для выполнения этой операции (требуется роль ADMIN)"
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}