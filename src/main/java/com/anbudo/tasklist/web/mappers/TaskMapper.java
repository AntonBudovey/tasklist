package com.anbudo.tasklist.web.mappers;

import com.anbudo.tasklist.domain.task.Task;
import com.anbudo.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {

}
