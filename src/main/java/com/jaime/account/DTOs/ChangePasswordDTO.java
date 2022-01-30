package com.jaime.account.DTOs;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ChangePasswordDTO {
    @Size(min = 12,message = "Password length must be 12 chars minimum!")
    private String new_password;
}
