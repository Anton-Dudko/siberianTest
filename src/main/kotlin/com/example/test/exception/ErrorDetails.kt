package com.example.test.exception

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Данные об ошибке")
data class ErrorDetails(
    val createdAt: LocalDateTime? = null,
    val message: String? = null
)
