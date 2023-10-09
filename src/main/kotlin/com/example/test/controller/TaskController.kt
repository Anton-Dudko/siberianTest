package com.example.test.controller

import com.example.test.model.entity.Task
import com.example.test.service.TaskService
import org.springframework.data.domain.Page
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
class TaskController(private val taskService: TaskService) {

    @GetMapping("/")
    fun view(
        model: Model,
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("size", defaultValue = "5") size: Int,
        @RequestParam("sortField", defaultValue = "id", required = false) sortField: String,
        @RequestParam("title", required = false, defaultValue = "") title: String?,
        @RequestParam("description", required = false, defaultValue = "") description: String?,
        @RequestParam("completed", required = false) completed: Boolean?,
        @RequestParam("fromDate", required = false) fromDate: LocalDate?,
        @RequestParam("toDate", required = false) toDate: LocalDate?
    ): String {
        val taskPage: Page<Task> = taskService.findPaginated(page - 1, size, sortField, title, description)
        var listTasks = taskPage.content

        model.addAttribute("title", title)
        model.addAttribute("description", description)
        model.addAttribute("size", size)
        model.addAttribute("taskPage", page)
        model.addAttribute("sortField", sortField)
        model.addAttribute("totalPages", taskPage.totalPages)
        model.addAttribute("totalItems", taskPage.totalElements)
        model.addAttribute("tasks", listTasks)

        return "index"
    }
}