package com.task.task_manage.repository

import com.task.task_manage.models.dto.TasksDTO
import com.task.task_manage.models.entity.Tasks
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TaskRepository: JpaRepository<Tasks, Long> {
    fun findByOwnerId(ownerId: Long) : List<Tasks>
    @Query("SELECT MAX(t.id) FROM Tasks t")
    fun findMaxId(): Long?
}