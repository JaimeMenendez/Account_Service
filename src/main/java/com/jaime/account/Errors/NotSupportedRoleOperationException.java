package com.jaime.account.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid operation!")
public class NotSupportedRoleOperationException extends RuntimeException{
}
