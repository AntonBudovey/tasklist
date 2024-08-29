package com.anbudo.tasklist.serviceImpl;

import com.anbudo.tasklist.config.TestConfig;
import com.anbudo.tasklist.domain.exception.ResourceNotFoundException;
import com.anbudo.tasklist.domain.task.Status;
import com.anbudo.tasklist.domain.task.Task;
import com.anbudo.tasklist.domain.task.TaskImage;
import com.anbudo.tasklist.repository.TaskRepository;
import com.anbudo.tasklist.repository.UserRepository;
import com.anbudo.tasklist.service.ImageService;
import com.anbudo.tasklist.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @MockBean
    private TaskRepository taskRepository;


    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.getById(id);
        Mockito.verify(taskRepository).findById(id);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void getByIdWithNoExistingId() {
        Long id = 1L;
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getAllByUserId() {
        Long id = 1L;
        List<Task> tasks = List.of(new Task(), new Task());
        Mockito.when(taskRepository.findAllByUserId(id))
                .thenReturn(tasks);
        List<Task> testTasks = taskService.getAllByUserId(id);
        Assertions.assertEquals(testTasks, tasks);
        Mockito.verify(taskRepository).findAllByUserId(id);

    }

}