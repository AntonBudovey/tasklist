package com.anbudo.tasklist.web.controller;

import com.anbudo.tasklist.domain.task.Task;
import com.anbudo.tasklist.domain.task.TaskImage;
import com.anbudo.tasklist.service.TaskService;
import com.anbudo.tasklist.web.dto.task.TaskDto;
import com.anbudo.tasklist.web.dto.task.TaskImageDto;
import com.anbudo.tasklist.web.dto.validation.OnCreate;
import com.anbudo.tasklist.web.dto.validation.OnUpdate;
import com.anbudo.tasklist.web.mappers.TaskImageMapper;
import com.anbudo.tasklist.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskImageMapper taskImageMapper;

    @GetMapping("/{id}")
    @QueryMapping(name ="taskById")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getById(@PathVariable @Argument Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @MutationMapping(name = "deleteTask")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void delete(@PathVariable @Argument Long id) {
        taskService.delete(id);
    }

    @PutMapping("/update")
    @MutationMapping(name = "updateTask")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#taskDto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody @Argument(name = "dto") TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void uploadImage(@PathVariable Long id, @Validated @ModelAttribute TaskImageDto imageDto) {
        TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(id, image);
    }
}
