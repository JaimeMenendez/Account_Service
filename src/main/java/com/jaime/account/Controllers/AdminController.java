package com.jaime.account.Controllers;

import com.jaime.account.DTOs.ChangeRoleDTO;
import com.jaime.account.DTOs.ChangeUserLockedStatusDTO;
import com.jaime.account.DTOs.UserDTO;
import com.jaime.account.Errors.RemoveAdministratorException;
import com.jaime.account.Errors.UserNotFoundException;
import com.jaime.account.Models.User;
import com.jaime.account.Services.LogService;
import com.jaime.account.Services.UserService;
import com.jaime.account.util.EventAction;
import com.jaime.account.util.RoleOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/admin/user")
public class AdminController {
    private final UserService userService;
    private final LogService logService;

    public AdminController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = userService.findAll();
        return ResponseEntity.ok(userList.stream().map(UserDTO::new).collect(Collectors.toList()));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email, @AuthenticationPrincipal UserDetails userLogged,
                                        HttpServletRequest request) {

        if (userLogged.getUsername().equals(email.toLowerCase(Locale.ROOT))) {
            throw new RemoveAdministratorException();
        }

        if (userService.deleteUserByEmail(email) <= 0) {
            throw new UserNotFoundException();
        }

        Map<String, String> body = Map.of("user", email.toLowerCase(Locale.ROOT), "status", "Deleted successfully!");

        logService.logAction(EventAction.DELETE_USER, userLogged.getUsername(), email.toLowerCase(Locale.ROOT), request.getRequestURI());
        return ResponseEntity.ok(body);
    }

    @PutMapping("/role")
    public ResponseEntity<?> updateUserRole(@RequestBody @Validated ChangeRoleDTO roleOp,
                                            @AuthenticationPrincipal UserDetails userLogged,
                                            HttpServletRequest request) {
        String prefixedRole = "ROLE_" + roleOp.getRole().trim();
        String operation = roleOp.getOperation().trim();
        User user = userService.updateRole(roleOp.getUser(), prefixedRole, operation, userLogged);

        // Logging action
        // At this point, all data from roleOp was validated and role action was successfully executed.
        EventAction action = EventAction.valueOf(operation + "_ROLE");
        RoleOperation op = RoleOperation.valueOf(operation);
        String joint = RoleOperation.GRANT == op ? "to" : "from";
        String subject = String.format("%s role %s %s %s", op.getActionNameCapitalized(), roleOp.getRole(), joint,
                roleOp.getUser().toLowerCase(Locale.ROOT));
        logService.logAction(action, userLogged.getUsername(), subject, request.getRequestURI());

        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/access")
    public ResponseEntity<?> changeUserAccess(@RequestBody @Valid ChangeUserLockedStatusDTO operation, HttpServletRequest request,
                                              @AuthenticationPrincipal UserDetails userLogged) {
        String username = operation.getUser().toLowerCase(Locale.ROOT);
        boolean isAccountNonLocked = operation.getOperation().equals("UNLOCK");
        userService.updateUserIsAccountNonLocked(username, isAccountNonLocked);

        Map<String, String> response = Map.of("status",
                String.format("User %s %sed!", username, operation.getOperation().toLowerCase(Locale.ROOT)));


        logService.logAction(isAccountNonLocked ? EventAction.UNLOCK_USER : EventAction.LOCK_USER,
                userLogged.getUsername(),
                operation.getOperationCapitalized() + " user " + username,
                request.getRequestURI());

        return ResponseEntity.ok(response);
    }


}
