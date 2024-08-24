package com.anbudo.tasklist.web.controller;

import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.service.AuthService;
import com.anbudo.tasklist.service.UserService;
import com.anbudo.tasklist.web.dto.auth.JwtRequest;
import com.anbudo.tasklist.web.dto.auth.JwtResponse;
import com.anbudo.tasklist.web.dto.user.UserDto;
import com.anbudo.tasklist.web.dto.validation.OnCreate;
import com.anbudo.tasklist.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest request) {
        JwtResponse response = authService.login(request);
        return response;
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userdto) {
        User user = userMapper.toEntity(userdto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }

}
