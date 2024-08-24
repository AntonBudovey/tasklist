package com.anbudo.tasklist.web.security.expression;

import com.anbudo.tasklist.domain.user.Role;
import com.anbudo.tasklist.service.UserService;
import com.anbudo.tasklist.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final UserService userService;

    public boolean canAccessUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();
        System.out.println(hasAnyRole(authentication, Role.ROLE_ADMIN));
        return (hasAnyRole(authentication, Role.ROLE_ADMIN) || userId.equals(id));
    }

    public boolean hasAnyRole(Authentication authentication, Role... roles) {
        for (Role role : roles) {
            System.out.println(role);
            System.out.println(authentication.getAuthorities());
            if (authentication.getAuthorities().contains(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean canAccessTask(Long taskId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        JwtEntity user = (JwtEntity) auth.getPrincipal();
        Long userId = user.getId();
        System.out.println((userService.isTaskOwner(userId, taskId) || hasAnyRole(auth, Role.ROLE_ADMIN)));

        return (userService.isTaskOwner(userId, taskId) || hasAnyRole(auth, Role.ROLE_ADMIN));
    }
}
