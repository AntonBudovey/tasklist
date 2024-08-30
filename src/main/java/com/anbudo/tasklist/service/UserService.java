package com.anbudo.tasklist.service;


import com.anbudo.tasklist.domain.user.Role;
import com.anbudo.tasklist.domain.user.User;

public interface UserService {

    User getById(Long id);
    User getByUsername(String username);
    User update(User user);
    User create(User user);
    boolean isTaskOwner(Long userId, Long taskId);
    void delete(Long id);

    User getTaskAuthor(Long id);
}
