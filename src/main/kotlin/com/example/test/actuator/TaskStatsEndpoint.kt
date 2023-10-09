package com.example.test.actuator

import com.example.test.service.TaskService
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.stereotype.Component

@Component
@WebEndpoint(id = "task-stats")
class TaskStatsEndpoint(private val taskService: TaskService) {

    @ReadOperation
    fun getTaskStats(): TaskStats {
        val createCount = taskService.getTaskCreateCount()
        val updateCount = taskService.getTaskUpdateCount()
        val deleteCount = taskService.getTaskDeleteCount()

        return TaskStats(createCount, updateCount, deleteCount)
    }
}