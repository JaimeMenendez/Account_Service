package com.jaime.account.DTOs;

import lombok.*;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserDTO {
    @NotBlank(message = "Name must not be null.")
    private String name;

    @NotBlank(message = "Lastname must not be null.")
    private String lastname;

    @Pattern(regexp = "[\\w.]+(@acme.com)$",message = "Email must be from acme.com domain")
    @Email(message = "You must enter a valid email")
    @NotBlank(message = "Email must not be null!")
    private String email;

    @NotBlank
    @Size(min = 12, message = "The password length must be at least 12 chars!")
    private String password;

}
