package com.example.shinra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Un Authorized Exception")
public class UnAuthorizedException extends RuntimeException {
}
