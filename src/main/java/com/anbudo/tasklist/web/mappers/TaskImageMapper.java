package com.anbudo.tasklist.web.mappers;

import com.anbudo.tasklist.domain.task.TaskImage;
import com.anbudo.tasklist.web.dto.task.TaskImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}
