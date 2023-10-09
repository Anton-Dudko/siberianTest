package com.example.test.controller

import com.example.test.exception.ErrorDetails
import com.example.test.model.dto.CreateTaskRequest
import com.example.test.model.dto.TaskDto
import com.example.test.model.dto.UpdateRequest
import com.example.test.model.entity.Task
import com.example.test.service.TaskService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
@Tag(name = "TaskApi", description = "Api для работы с задачами")
class TaskRestController(private val taskService: TaskService) {

    @Operation(summary = "Создание новой задачи")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201",description = "Задача успешно создана",content = [Content(schema = Schema(implementation = Task::class))]),    ApiResponse(responseCode = "201",description = "Задача успешно создана",content = [Content(schema = Schema(implementation = Task::class))]),
        ApiResponse(responseCode = "400 Bad Request",description = "Задача не создана", content = [Content(schema = Schema(implementation = ErrorDetails::class))]),
        ApiResponse(responseCode = "400 ",description = "Задача с таким названием уже существует", content = [Content(schema = Schema(implementation = ErrorDetails::class))])
    ])
    @Parameters(
        value = [
            Parameter(name = "title", description = "Название задачи", required = true),
            Parameter(name = "description", description = "Описание задачи", required = true)
        ]
    )
    @PostMapping
    fun create(@Valid @RequestBody request: CreateTaskRequest): Task {
        return taskService.create(request)
    }

    @Operation(summary = "Пометить задачу, как выполненную")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Задача успешно выполнена", content = [Content(schema = Schema(implementation = Task::class))]),
        ApiResponse(responseCode = "400 Bad Request", description = "Задача уже выполнена", content = [Content(schema = Schema(implementation = ErrorDetails::class))]),
        ApiResponse(responseCode = "404 Not Found", description = "Задача не найдена", content = [Content(schema = Schema(implementation = ErrorDetails::class))])
    ])
    @Parameter(name = "id", description = "Идентификатор задачи", required = true)
    @PutMapping("/{id}/complete")
    fun complete(@PathVariable id: Long): TaskDto? {
        return taskService.complete(id)
    }

    @Operation(summary = "Удаление задачи")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Задача успешно удалена"),
        ApiResponse(responseCode = "400 Bad Request", description = "Выполненная задача не может быть удалена", content = [Content(schema = Schema(implementation = ErrorDetails::class))]),
        ApiResponse(responseCode = "404 Not Found", description = "Задача не найдена", content = [Content(schema = Schema(implementation = ErrorDetails::class))])
    ] )
    @Parameter(name = "id", description = "Идентификатор задачи", required = true)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        taskService.deleteById(id)
    }

    @Operation(summary = "Изменение названия и/или описания задачи")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Задача успешно изменена", content = [Content(schema = Schema(implementation = Task::class))]),
        ApiResponse(responseCode = "404 Not Found", description = "Задача не найдена", content = [Content(schema = Schema(implementation = ErrorDetails::class))]),
        ApiResponse(responseCode = "400 Bad Request", description = "Выполненная задача не может быть изменена", content = [Content(schema = Schema(implementation = ErrorDetails::class))]),
        ApiResponse(responseCode = "400",description = "Задача с таким названием уже существует", content = [Content(schema = Schema(implementation = ErrorDetails::class))])
    ])
    @Parameters(
        value = [
            Parameter(name = "id", description = "Идентификатор задачи", required = true),
            Parameter(name = "title", description = "Название задачи", required = false),
            Parameter(name = "description", description = "Описание задачи", required = false)
        ]
    )
    @PatchMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody updateRequest: UpdateRequest): TaskDto {
        return taskService.update(id, updateRequest)
    }
}