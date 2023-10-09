package com.example.test.repository

import com.example.test.model.entity.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import java.util.function.Predicate
import javax.print.attribute.standard.DateTimeAtCompleted

interface TaskRepository : PagingAndSortingRepository<Task, Long>, CrudRepository<Task, Long> {
    fun findTaskByTitle(title: String?): Task?
    fun findAllByTitleContainsAndDescriptionContains(title: String? = "", description: String? = "", pageable: Pageable): Page<Task>

    //    Не работает!!!!!
//    @Query("SELECT t FROM task t WHERE (:title IS NULL OR t.title LIKE :title) AND (:description IS NULL OR t.description LIKE :description) " )
//    fun findByParams(@Param("title") title: String?, @Param("description")description: String?, pageable: Pageable):List<Task>

}
