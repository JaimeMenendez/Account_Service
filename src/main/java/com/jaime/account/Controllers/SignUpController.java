package com.jaime.account.Controllers;

import com.jaime.account.DTOs.*;
import com.jaime.account.Errors.*;
import com.jaime.account.Services.LogService;
import com.jaime.account.util.EventAction;
import com.jaime.account.util.Role;
import com.jaime.account.Models.User;
import com.jaime.account.Repository.BreachedPasswordsRepository;
import com.jaime.account.Services.PaymentService;
import com.jaime.account.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@RestController
@Validated
public class SignUpController {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final BreachedPasswordsRepository breachedPasswordsRepository;
    static private boolean isFirstUser = true;
    private final LogService logService;

    public SignUpController(UserService userService, PasswordEncoder encoder,
                            BreachedPasswordsRepository breachedPasswordsRepository, PaymentService paymentService,
                            LogService logService) {
        this.userService = userService;
        this.encoder = encoder;
        this.breachedPasswordsRepository = breachedPasswordsRepository;
        this.logService = logService;
    }


    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> singUpNewUser(@Valid @RequestBody NewUserDTO newUser, HttpServletRequest request) {

        if (userService.existsByEmail(newUser.getEmail())) {
            throw new UserExistException();
        }

        if (breachedPasswordsRepository.isBreachedPassword(newUser.getPassword())) {
            throw new BreachedPasswordException();
        }

        User user = User.builder()
                .email(newUser.getEmail().toLowerCase(Locale.ROOT))
                .password(encoder.encode(newUser.getPassword()))
                .name(newUser.getName())
                .lastname(newUser.getLastname())
                .roles(Set.of(isFirstUser ? Role.ROLE_ADMINISTRATOR : Role.ROLE_USER))
                .build();

        isFirstUser = false;

        User userCreated = null;
        try {
            userCreated = userService.save(user);
        } catch (Exception e) {
            throw new UserExistException();
        }

        logService.logAction(EventAction.CREATE_USER, "Anonymous", userCreated.getEmail(), request.getRequestURI());
        return ResponseEntity.ok(new UserDTO(userCreated));
    }

    @PostMapping("/api/auth/changepass")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody ChangePasswordDTO newPassword,
                                                @AuthenticationPrincipal UserDetails userLogged,
                                                HttpServletRequest request) {
        if (breachedPasswordsRepository.isBreachedPassword(newPassword.getNew_password())) {
            throw new BreachedPasswordException();
        }

        if (encoder.matches(newPassword.getNew_password(), userLogged.getPassword())) {
            throw new SamePasswordException();
        }

        User user = userService.findFirstByEmail(userLogged.getUsername());
        user.setPassword(encoder.encode(newPassword.getNew_password()));
        userService.save(user);

        logService.logAction(EventAction.CHANGE_PASSWORD, userLogged.getUsername(),
                userLogged.getUsername(), request.getRequestURI());

        return new ResponseEntity<>(Map.of("email", user.getEmail(),
                "status", "The password has been updated successfully"), HttpStatus.OK);
    }
}
