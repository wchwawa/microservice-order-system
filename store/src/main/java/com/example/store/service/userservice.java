package com.example.store.service;

import com.example.store.entity.user;
import com.example.store.mapper.usermapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Service
public class userservice implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(userservice.class);

    @Autowired
    private usermapper usermapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user user = usermapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        // log user information
        logger.debug("User found: username={}, passwordHash={}", user.getUsername(), user.getPasswordHash());
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
                throw new InternalAuthenticationServiceException("user has no password set");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public boolean isUsernameExists(String username) {
        user existingUser = usermapper.findByUsername(username);
        return existingUser != null;
    }


    // register a new user
    public void register(user user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        usermapper.insertUser(user);
    }
}
