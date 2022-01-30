package com.jaime.account.Services;

import com.jaime.account.Errors.*;
import com.jaime.account.util.Role;
import com.jaime.account.Models.User;
import com.jaime.account.Repository.UserRepository;
import com.jaime.account.util.RoleOperation;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    final private UserRepository userService;

    public UserServiceImpl(UserRepository userService) {
        this.userService = userService;
    }

    public User findFirstByEmail(String email) {
        return userService.findFirstByEmail(email.toLowerCase(Locale.ROOT));
    }

    public boolean existsByEmail(String email) {
        return this.userService.existsByEmail(email);
    }

    @Transactional
    public long deleteUserByEmail(String email) {
        return this.userService.deleteUserByEmail(email);
    }

    public User updateRole(String email, String roleString, String operation, UserDetails admin) {
        // Validate Role
        Role role = null;
        try {
            role = Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundException();
        }

        // Validate Role operation
        RoleOperation roleOp = null;
        try {
            roleOp = RoleOperation.valueOf(operation);
        } catch (IllegalArgumentException e) {
            throw new NotSupportedRoleOperationException();
        }

        // Validate User Exist
        User user = userService.findFirstByEmail(email.toLowerCase(Locale.ROOT));
        if (user == null) {
            throw new UserNotFoundException();
        }

        Set<Role> roles = user.getRoles();
        switch (roleOp) {
            case REMOVE:
                if (role == Role.ROLE_ADMINISTRATOR) {
                    throw new RemoveAdministratorException();
                }

                if (!roles.contains(role)) {
                    throw new UserHasNotRoleSuchException();
                }

                if (roles.size() <= 1) {
                    throw new EmptyRoleException();
                }

                user.removeRole(role);
                break;

            case GRANT:
                if (role == Role.ROLE_ADMINISTRATOR || roles.contains(Role.ROLE_ADMINISTRATOR)) {
                    throw new CombinedRolesException();
                }
                user.addRole(role);
                break;
        }
        return userService.save(user);
    }

    public List<User> findAll() {
        return userService.findAll();
    }

    public User save(User user) {
        return userService.save(user);
    }

    public void updateUserIsAccountNonLocked(String email, boolean isAccountNonLocked) {
        User user = findFirstByEmail(email);
        if (user.hasRole(Role.ROLE_ADMINISTRATOR)) {
            throw new BlockAdministratorException();
        }
        userService.updateUserIsAccountNonLocked(isAccountNonLocked, email);
    }

    @Override
    public void setLoginAttemptsCounter(String userEmail,  int value) {
        userService.setLoginAttemptsCounter(value,userEmail);
    }
}
