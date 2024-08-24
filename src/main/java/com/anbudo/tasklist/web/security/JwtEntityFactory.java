package com.anbudo.tasklist.web.security;

import com.anbudo.tasklist.domain.user.User;

public class JwtEntityFactory {
    public static JwtEntity create(User user) {
        return new JwtEntity(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                user.getRoles()
        );
    }
}
