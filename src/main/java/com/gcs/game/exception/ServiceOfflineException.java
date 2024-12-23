package com.gcs.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE,reason = "SERVICE_OFFLINE")
public class ServiceOfflineException extends Exception {
}
