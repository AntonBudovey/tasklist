package com.anbudo.tasklist.web.controller;

import com.anbudo.tasklist.domain.task.Task;
import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.service.TaskService;
import com.anbudo.tasklist.service.UserService;
import com.anbudo.tasklist.web.dto.task.TaskDto;
import com.anbudo.tasklist.web.dto.user.UserDto;
import com.anbudo.tasklist.web.dto.validation.OnCreate;
import com.anbudo.tasklist.web.dto.validation.OnUpdate;
import com.anbudo.tasklist.web.mappers.TaskMapper;
import com.anbudo.tasklist.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userservice;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public UserDto getById(@PathVariable Long id) {
        User user = userservice.getById(id);
        return userMapper.toDto(user);
    }

    @GetMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public List<TaskDto> getAllUserTasks(@PathVariable Long id) {
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void delete(@PathVariable Long id) {
        userservice.delete(id);
    }

    @PutMapping("/update")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userDto.id)")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updatedUser = userservice.update(user);
        return userMapper.toDto(updatedUser);
    }
    @PostMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public TaskDto create(@Validated(OnCreate.class) @RequestBody TaskDto taskDto, @PathVariable Long id) {
        Task task = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }
}
