package com.jaime.account.Repository;

import com.jaime.account.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import javax.validation.constraints.Email;


public interface UserRepository extends JpaRepository<User,Long> {
    User findFirstByEmail(String email);
    boolean existsByEmail(String email);
    Long deleteUserByEmail(String email);

    boolean existsAllByEmail(@Email String email);

    @Modifying
    @Transactional
    @Query("update user set isAccountNonLocked = ?1 where email = ?2")
    void updateUserIsAccountNonLocked (boolean isAccountNonLocked, String email);

    @Modifying
    @Transactional
    @Query("update user set loginAttempts = ?1 where email = ?2")
    void setLoginAttemptsCounter (int value, String userEmail);

}
