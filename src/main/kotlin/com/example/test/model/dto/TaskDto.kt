package com.example.test.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "Информация о задаче")
data class TaskDto(
    val id: Long? = null,
    var title: String,
    var description: String?,
    var creationDate: LocalDate?,
    var modificationDate: LocalDate?,
    var isCompleted: Boolean
)

