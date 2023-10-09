package com.example.test.model.entity

import org.springframework.data.annotation.Id
import java.time.LocalDate

data class Task(
    @Id
    val id: Long? = null,
    val title: String,
    val description: String?,
    val creationDate: LocalDate?,
    val modificationDate: LocalDate?,
    val isCompleted: Boolean
)

