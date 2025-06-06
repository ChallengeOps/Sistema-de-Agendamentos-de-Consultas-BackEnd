package com.sistema_de_agendamentos.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> responseStatusException(ResponseStatusException ex, WebRequest request) {
        var responseError = new ResponseError((HttpStatus) ex.getStatusCode(), ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(responseError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseError> handleConstraintViolation(ConstraintViolationException ex) {
        String mensagem = "Violação de restrição do banco de dados.";
        if (ex.getConstraintName() != null && ex.getConstraintName().toLowerCase().contains("unique")) {
            mensagem = "Já existe um registro com este valor.";
        }
        ResponseError responseError = new ResponseError(HttpStatus.CONFLICT, mensagem);
        return new ResponseEntity<>(responseError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = String.join(", ", errors);
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST, errorMessage);
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> handleEntityNotFound(EntityNotFoundException ex) {
        ResponseError responseError = new ResponseError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> handleIllegalArgument(IllegalArgumentException ex) {
        ResponseError responseError = new ResponseError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException ex) {
        ResponseError responseError = new ResponseError(HttpStatus.FORBIDDEN, "Acesso negado.");
        return new ResponseEntity<>(responseError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensagem = "Violação de restrição de valor único.";
        if (ex.getRootCause() != null && ex.getRootCause().getMessage() != null &&
                ex.getRootCause().getMessage().toLowerCase().contains("unique")) {
            mensagem = "Já existe um registro com este valor.";
        }
        ResponseError responseError = new ResponseError(HttpStatus.CONFLICT, mensagem);
        return new ResponseEntity<>(responseError, HttpStatus.CONFLICT);
    }
}