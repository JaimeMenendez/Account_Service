package com.jaime.account.Listener;

import com.jaime.account.Models.User;
import com.jaime.account.Services.LogService;
import com.jaime.account.Services.UserService;
import com.jaime.account.util.EventAction;
import com.jaime.account.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LogService logService;
    private final UserService userService;

    private final HttpServletRequest request;

    @Autowired
    public AuthenticationFailureListener(LogService logService, UserService userService, HttpServletRequest request) {
        this.logService = logService;
        this.userService = userService;
        this.request = request;
    }

    @Override
    @Transactional
    @Modifying
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = Optional.of(event.getAuthentication().getName()).orElse("Anonymous");
        String path = request.getRequestURI();
        logService.logAction(EventAction.LOGIN_FAILED, username, path, path);

        User user = userService.findFirstByEmail(username);
        if (user != null && !user.hasRole(Role.ROLE_ADMINISTRATOR)) {
            int loginAttempt = user.getLoginAttempts() + 1;
            if (loginAttempt >= 5) {
                userService.updateUserIsAccountNonLocked(user.getEmail(), false);
                logService.logAction(EventAction.BRUTE_FORCE, username, path, path);
                logService.logAction(EventAction.LOCK_USER, username, "Lock user " + username,"/api/admin/user/access");
                loginAttempt = 0;
            }
            userService.setLoginAttemptsCounter(user.getEmail(), loginAttempt);
        }
    }
}
