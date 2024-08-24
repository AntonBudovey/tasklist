package com.anbudo.tasklist.web.dto.task;

import com.anbudo.tasklist.domain.task.Status;
import com.anbudo.tasklist.web.dto.validation.OnCreate;
import com.anbudo.tasklist.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    @NotNull(message = "Id cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "Title cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max=255, message = "Title cannot be longer than 255 characters", groups = {OnUpdate.class, OnCreate.class})
    private String title;

    @Length(max=255, message = "Description cannot be longer than 255 characters", groups = {OnUpdate.class, OnCreate.class})
    private String description;

    private Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images;
}
