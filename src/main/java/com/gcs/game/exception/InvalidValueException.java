package com.gcs.game.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "INVALID_VALUE")
public class InvalidValueException extends Exception {
}