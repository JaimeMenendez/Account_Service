package com.jaime.account.Services;

import com.jaime.account.Models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    final UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findFirstByEmail(username.toLowerCase(Locale.ROOT));
        if(user == null){
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }
}
