package com.anbudo.tasklist.web.dto.user;

import com.anbudo.tasklist.web.dto.validation.OnCreate;
import com.anbudo.tasklist.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class UserDto {
    @NotNull(message = "Id cannot be null", groups = {OnUpdate.class})
    private Long id;
    @NotNull(message = "Name cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max=255, message = "Name cannot be longer than 255 characters", groups = {OnUpdate.class, OnCreate.class})
    private String name;
    @NotNull(message = "Username cannot be null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max=255, message = "Username cannot be longer than 255 characters", groups = {OnUpdate.class, OnCreate.class})
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password cannot be null", groups = {OnUpdate.class, OnCreate.class})
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password cannot be null", groups = {OnCreate.class})
    private String passwordConfirm;
}
