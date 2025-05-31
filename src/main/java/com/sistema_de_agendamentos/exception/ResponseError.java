package com.sistema_de_agendamentos.exception;

import org.springframework.http.HttpStatus;

public record ResponseError(HttpStatus httpStatus, String message) {
}
