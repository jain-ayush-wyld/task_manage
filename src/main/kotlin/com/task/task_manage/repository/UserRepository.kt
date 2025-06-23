package com.task.task_manage.repository

import com.task.task_manage.models.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<Users, Long> {
    fun findByEmail(email: String): Users?
}