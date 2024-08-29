package com.anbudo.tasklist.service.impl;

import com.anbudo.tasklist.domain.exception.ResourceNotFoundException;
import com.anbudo.tasklist.domain.task.Status;
import com.anbudo.tasklist.domain.task.Task;
import com.anbudo.tasklist.domain.task.TaskImage;
import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.repository.TaskRepository;
import com.anbudo.tasklist.service.ImageService;
import com.anbudo.tasklist.service.TaskService;
import com.anbudo.tasklist.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "taskService::getById", key = "#id")
    public Task getById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    @CachePut(value = "taskService::getById", key = "#task.id")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    @Transactional
    @Cacheable(value = "taskService::getById",
            condition ="#task.id!=null",
            key = "#task.id")
    public Task create(Task task, Long userId) {
        task.setStatus(Status.TODO);
        taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());
        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "taskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "taskService::getById", key = "#id")
    public void uploadImage(Long id, TaskImage image) {
        Task task = getById(id);
        String fileName = imageService.upload(image);
        task.getImages().add(fileName);
        update(task);

    }
}
