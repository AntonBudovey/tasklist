package com.anbudo.tasklist.service.impl;

import com.anbudo.tasklist.domain.MailType;
import com.anbudo.tasklist.domain.task.Task;
import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.service.MailService;
import com.anbudo.tasklist.service.Reminder;
import com.anbudo.tasklist.service.TaskService;
import com.anbudo.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {
    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private final Duration DURATION = Duration.ofHours(1);

    @Scheduled(cron = "0 * * * * *")
    @Override
    public void remindForTask() {
        List<Task> tasks = taskService.getAllSoonTasks(DURATION);
        tasks.forEach(task -> {
            User user = userService.getTaskAuthor(task.getId());
            Properties params = new Properties();
            params.setProperty("task.title", task.getTitle());
            params.setProperty("task.description", task.getDescription());
            mailService.sendEmail(user, MailType.REMINDER, params);
        });
    }
}
