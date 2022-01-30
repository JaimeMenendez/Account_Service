package com.jaime.account.DTOs;

import com.jaime.account.Models.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private List<String> roles;

    public UserDTO(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.roles = user.getRolesString().stream()
                .sorted().collect(Collectors.toList());
    }
}
