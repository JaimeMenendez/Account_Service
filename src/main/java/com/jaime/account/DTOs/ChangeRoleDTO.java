package com.jaime.account.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeRoleDTO {
    private String user;
    private String role;
    private String operation;
}
