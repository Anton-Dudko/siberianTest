package com.example.test.util

import com.example.test.model.dto.TaskDto
import com.example.test.model.entity.Task

fun Task.toDto(): TaskDto {
    return TaskDto(
        id = id,
        title = title,
        description = description,
        creationDate = creationDate,
        modificationDate = modificationDate,
        isCompleted = isCompleted
    )
}