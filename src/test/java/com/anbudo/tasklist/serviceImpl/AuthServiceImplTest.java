package com.anbudo.tasklist.serviceImpl;

import com.anbudo.tasklist.config.TestConfig;
import com.anbudo.tasklist.domain.user.Role;
import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.repository.UserRepository;
import com.anbudo.tasklist.service.AuthService;
import com.anbudo.tasklist.service.UserService;
import com.anbudo.tasklist.service.impl.AuthServiceImpl;
import com.anbudo.tasklist.web.dto.auth.JwtRequest;
import com.anbudo.tasklist.web.dto.auth.JwtResponse;
import com.anbudo.tasklist.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private UserService userService;

    @Test
    void successLogin() {
        String successAccessToken = "successAccessToken";
        String successRefreshToken = "successRefreshToken";
        Long id = 1L;
        Set<Role> roles = Set.of(Role.ROLE_USER);
        String username = "username";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setRoles(roles);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(null);
        Mockito.when(tokenProvider.createAccessToken(id, username, roles)).thenReturn(successAccessToken);
        Mockito.when(tokenProvider.createRefreshToken(id, username)).thenReturn(successRefreshToken);
        JwtRequest request = new JwtRequest();
        request.setPassword("password");
        request.setUsername(username);

        JwtResponse response = authService.login(request);
        Assertions.assertEquals(response.getUsername(), username);
        Assertions.assertEquals(response.getId(), id);
        Assertions.assertEquals(response.getAccessToken(), successAccessToken);
        Assertions.assertEquals(response.getRefreshToken(), successRefreshToken);
        Mockito.verify(authenticationManager).authenticate(Mockito.any());
        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(tokenProvider).createAccessToken(id, username, roles);
        Mockito.verify(tokenProvider).createRefreshToken(id, username);
    }

    @Test
    void failLogin() {
        Long id = 1L;
        Set<Role> roles = Set.of(Role.ROLE_USER);
        String username = "username";
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setRoles(roles);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        JwtRequest request = new JwtRequest();
        request.setPassword("failPassword");
        request.setUsername(username);
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(BadCredentialsException.class);
        Assertions.assertThrows(AuthenticationException.class, () -> authService.login(request));
    }
}
