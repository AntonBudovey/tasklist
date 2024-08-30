package com.anbudo.tasklist.service;

import com.anbudo.tasklist.domain.MailType;
import com.anbudo.tasklist.domain.user.User;

import java.util.Properties;

public interface MailService {
    void sendEmail(User receiver, MailType type, Properties params);
}
