package com.anbudo.tasklist.service.impl;

import com.anbudo.tasklist.domain.exception.AccessDeniedException;
import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.repository.TaskRepository;
import com.anbudo.tasklist.repository.UserRepository;
import com.anbudo.tasklist.service.AuthService;
import com.anbudo.tasklist.service.MailService;
import com.anbudo.tasklist.service.UserService;
import com.anbudo.tasklist.web.dto.auth.JwtRequest;
import com.anbudo.tasklist.web.dto.auth.JwtResponse;
import com.anbudo.tasklist.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final AuthenticationManager manager;
    private final JwtTokenProvider provider;
    @Override
    public JwtResponse login(JwtRequest request) {
        JwtResponse response = new JwtResponse();
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userService.getByUsername(request.getUsername());
        response.setUsername(user.getUsername());
        response.setId(user.getId());
        response.setAccessToken(provider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        response.setRefreshToken(provider.createRefreshToken(user.getId(), user.getUsername()));
        return response;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return provider.refreshUserTokens(refreshToken);
    }
}
