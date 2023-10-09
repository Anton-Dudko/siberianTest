package com.example.test.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

@Schema(description = "Данные для создания задачи")
data class CreateTaskRequest(

    @field:NotEmpty
    @field:Size(max = 256)
    var title: String,

    @field:NotEmpty
    @field:Size(max = 10240)
    var description: String?
)