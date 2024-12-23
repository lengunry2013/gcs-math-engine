package com.gcs.game.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED,reason = "AUTH_FAILED")
public class AuthenticationFailException extends Exception {
}
