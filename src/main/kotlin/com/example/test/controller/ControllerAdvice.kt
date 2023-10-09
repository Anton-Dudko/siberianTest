package com.example.test.controller

import com.example.test.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(TaskNotCreatedException::class)
    fun handleTaskNotCreatedException(e: TaskNotCreatedException): ResponseEntity<ErrorDetails> {
        return ResponseEntity(ErrorDetails(LocalDateTime.now(), e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TaskDuplicateException::class)
    fun handleTaskDuplicateException(e: TaskDuplicateException): ResponseEntity<ErrorDetails> {
        return ResponseEntity(ErrorDetails(LocalDateTime.now(), e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TaskNotUpdatedException::class)
    fun handleTaskNotUpdatedException(e: TaskNotUpdatedException): ResponseEntity<ErrorDetails> {
        return ResponseEntity(ErrorDetails(LocalDateTime.now(), e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFoundException(e: TaskNotFoundException): ResponseEntity<ErrorDetails> {
        return ResponseEntity(ErrorDetails(LocalDateTime.now(), e.message), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(TaskNotDeletedException::class)
    fun handleTaskNotDeletedException(e: TaskNotDeletedException): ResponseEntity<ErrorDetails> {
        return ResponseEntity(ErrorDetails(LocalDateTime.now(), e.message), HttpStatus.BAD_REQUEST)
    }
}