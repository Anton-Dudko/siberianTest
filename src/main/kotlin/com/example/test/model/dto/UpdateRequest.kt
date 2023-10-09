package com.example.test.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

@Schema(description = "Данные для изменения названия и/или описания")
data class UpdateRequest(
    @field:Size(max = 256)
    val title: String? = null,
    @field:Size(max = 10240)
    val description: String? = null
)