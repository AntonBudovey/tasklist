package com.anbudo.tasklist.config;

import com.anbudo.tasklist.repository.TaskRepository;
import com.anbudo.tasklist.repository.UserRepository;
import com.anbudo.tasklist.service.ImageService;
import com.anbudo.tasklist.service.impl.AuthServiceImpl;
import com.anbudo.tasklist.service.impl.MailServiceImpl;
import com.anbudo.tasklist.service.impl.TaskServiceImpl;
import com.anbudo.tasklist.service.impl.UserServiceImpl;
import com.anbudo.tasklist.service.props.JwtProperties;
import com.anbudo.tasklist.service.props.MinioProperties;
import com.anbudo.tasklist.web.security.JwtTokenProvider;
import com.anbudo.tasklist.service.impl.ImageServiceImpl;
import com.anbudo.tasklist.web.security.JwtUserDetailsService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.TemplateEngine;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    @Bean
    @Primary
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(
                "dmdqYmhqbmttYmNhamNjZWhxa25hd2puY2xhZWtic3ZlaGtzYmJ1dg=="
        );
        return jwtProperties;
    }

    @Bean
    public UserDetailsService userDetailsService(
            final UserRepository userRepository
    ) {
        return new JwtUserDetailsService(userService(userRepository));
    }

    @Bean
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        MinioProperties properties = new MinioProperties();
        properties.setBucket("images");
        return properties;
    }

    @Bean
    public TemplateEngine templateEngine() {
        return Mockito.mock(TemplateEngine.class);
    }

    @Bean
    public JavaMailSender mailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    @Primary
    public MailServiceImpl mailService() {
        return new MailServiceImpl(mailSender(),templateEngine());
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new ImageServiceImpl(minioClient(), minioProperties());
    }

    @Bean
    public JwtTokenProvider tokenProvider(
            final UserRepository userRepository
    ) {
        return new JwtTokenProvider(jwtProperties(),
                userDetailsService(userRepository),
                userService(userRepository));
    }

    @Bean
    @Primary
    public UserServiceImpl userService(
            final UserRepository userRepository
    ) {
        return new UserServiceImpl(
                userRepository,
                testPasswordEncoder(),
                mailService()
        );
    }

    @Bean
    @Primary
    public TaskServiceImpl taskService(
            final TaskRepository taskRepository
    ) {
        return new TaskServiceImpl(taskRepository, imageService());
    }

    @Bean
    @Primary
    public AuthServiceImpl authService(
            final UserRepository userRepository,
            final AuthenticationManager authenticationManager
    ) {
        return new AuthServiceImpl(
                userService(userRepository),
                authenticationManager,
                tokenProvider(userRepository)
        );
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }

}