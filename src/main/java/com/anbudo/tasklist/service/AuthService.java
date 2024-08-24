package com.anbudo.tasklist.service;

import com.anbudo.tasklist.web.dto.auth.JwtRequest;
import com.anbudo.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest request);
    JwtResponse refresh(String refreshToken);
}
