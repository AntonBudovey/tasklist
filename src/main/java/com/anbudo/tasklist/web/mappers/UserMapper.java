package com.anbudo.tasklist.web.mappers;

import com.anbudo.tasklist.domain.user.User;
import com.anbudo.tasklist.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
