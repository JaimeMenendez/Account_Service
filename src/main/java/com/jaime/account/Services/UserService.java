package com.jaime.account.Services;

import com.jaime.account.Models.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService {
    User findFirstByEmail(String email);
    boolean existsByEmail(String email);
    @Transactional
    long deleteUserByEmail(String email);
    User updateRole(String email, String roleString, String operation, UserDetails admin);
    List<User> findAll();
    User save(User user);
    void updateUserIsAccountNonLocked(String email, boolean isAccountNonLocked);
    void setLoginAttemptsCounter (String userEmail, int value);
}
