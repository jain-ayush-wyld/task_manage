package com.task.task_manage.models.model

import org.springframework.data.annotation.Id
import java.time.Instant
import javax.annotation.processing.Generated


data class Task(
    val title: String,
    val content: String,
    val color: Long,
    val createdAt: Instant,
    val ownerId: String,
    @Id
    @Generated
    val id:Long
)
