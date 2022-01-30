package com.jaime.account.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Can't remove ADMINISTRATOR role!")
public class RemoveAdministratorException extends RuntimeException{
}
