package com.jaime.account.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User LOCKED!")
public class UserLockedException extends RuntimeException {
}
