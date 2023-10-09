package com.example.test.service

import com.example.test.exception.*
import com.example.test.model.dto.CreateTaskRequest
import com.example.test.model.dto.TaskDto
import com.example.test.model.dto.UpdateRequest
import com.example.test.model.entity.Task
import com.example.test.repository.TaskRepository
import com.example.test.util.toDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class TaskService(
    private val taskRepository: TaskRepository
) {
    private var taskCreateCount = taskRepository.findAll().count()
    private var taskUpdateCount = 0
    private var taskDeleteCount = 0

    fun create(request: CreateTaskRequest): Task {
        if (taskRepository.findTaskByTitle(request.title) != null)
            throw TaskDuplicateException("Task with the same title already exists!")
        try {
            val task = Task(
                title = request.title,
                description = request.description,
                creationDate = LocalDate.now(),
                modificationDate = null,
                isCompleted = false
            )
            taskCreateCount++
            return taskRepository.save(task)
        } catch (e: RuntimeException) {
            throw TaskNotCreatedException("Task not created!")
        }

    }

    fun update(id: Long, request: UpdateRequest): TaskDto {
        if (taskRepository.findTaskByTitle(request.title) != null)
            throw TaskDuplicateException("Task with the same title already exists!")
        val task = taskRepository.findById(id).orElse(null)
        if (task != null) {
            if (!task.isCompleted) {
                val updated = task.copy(
                    title = request.title ?: task.title,
                    description = request.description ?: task.description,
                    modificationDate = LocalDate.now()
                )
                taskUpdateCount++
                return taskRepository.save(updated).toDto()
            } else {
                throw TaskNotUpdatedException("A completed task cannot be updated!")
            }
        } else {
            throw TaskNotFoundException("Task not found!")
        }
    }

    fun deleteById(id: Long) {
        var task = taskRepository.findById(id).orElse(null)
        if (task != null) {
            if (!task.isCompleted) {
                taskDeleteCount++
                taskRepository.deleteById(id)
            } else
                throw TaskNotDeletedException("A completed task cannot be deleted!")
        } else
            throw TaskNotFoundException("Task not found!")

    }

    fun complete(id: Long): TaskDto? {
        var task = taskRepository.findById(id).orElse(null)
        if (task != null) {
            if (!task.isCompleted) {
                val completedTask = task.copy(isCompleted = true, modificationDate = LocalDate.now())
                return taskRepository.save(completedTask).toDto()
            }
            return throw TaskNotUpdatedException("The task has already been completed")
        } else {
            throw TaskNotFoundException("Task not found!")
        }
    }

    fun findPaginated(page: Int, size: Int, sortField: String, title: String?, description: String?): Page<Task> {
        val sort: Sort = Sort.by(sortField).ascending()
        return taskRepository.findAllByTitleContainsAndDescriptionContains(title, description, PageRequest.of(page, size, sort))
    }

    fun getTaskCreateCount(): Int {
        return taskCreateCount
    }

    fun getTaskUpdateCount(): Int {
        return taskUpdateCount
    }

    fun getTaskDeleteCount(): Int {
        return taskDeleteCount
    }
}