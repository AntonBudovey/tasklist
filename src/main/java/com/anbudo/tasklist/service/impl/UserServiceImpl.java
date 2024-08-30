package com.anbudo.tasklist.service.impl;

import com.anbudo.tasklist.domain.MailType;
import com.anbudo.tasklist.domain.exception.ResourceNotFoundException;
import com.anbudo.tasklist.domain.user.Role;
import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.repository.UserRepository;
import com.anbudo.tasklist.service.MailService;
import com.anbudo.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final MailService mailService;
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userService::getById", key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userService::getByUsername", key = "#username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(cacheNames = "userService::getById", key = "#user.id"),
            @CachePut(cacheNames = "userService::getByUsername", key = "#user.username")
    })
    public User update(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    @Caching(cacheable = {
            @Cacheable(
                    value = "UserService::getById",
                    condition = "#user.id!=null",
                    key = "#user.id"
            ),
            @Cacheable(
                    value = "UserService::getByUsername",
                    condition = "#user.username!=null",
                    key = "#user.username"
            )
    })
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            throw new IllegalStateException("Passwords do not match");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);
        mailService.sendEmail(user, MailType.REGISTRATION, new Properties());
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userService::isTaskOwner", key = "#userId + '.' + #taskId")
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userService::getById", key = "#id")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "UserService::getTaskAuthor",
            key = "#taskId"
    )
    public User getTaskAuthor(Long taskId) {
        return userRepository.findTaskAuthor(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
