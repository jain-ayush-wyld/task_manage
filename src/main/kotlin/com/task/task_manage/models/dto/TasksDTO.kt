package com.task.task_manage.models.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.time.Instant

data class TasksDTO(

    val title: String,
    val description: String,
    val color: Long,
//    @field:NotBlank(message="title cannot be null ")
//    @field:NotEmpty(message="title cannot be blank")
    val ownerId: String,
    val createdAt: Instant,
    val id: Long
    )

//may be need to put ? in all fields !!!!!