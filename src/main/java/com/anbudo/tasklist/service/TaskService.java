package com.anbudo.tasklist.service;

import com.anbudo.tasklist.domain.task.Task;
import com.anbudo.tasklist.domain.task.TaskImage;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task getById(Long id);
    List<Task> getAllByUserId(Long userId);
    Task update(Task task);

    Task create(Task task, Long userId);
    void delete(Long id);
    void uploadImage(Long id, TaskImage image);

    List<Task> getAllSoonTasks(Duration duration);
}
