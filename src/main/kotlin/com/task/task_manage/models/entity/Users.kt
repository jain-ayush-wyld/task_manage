package com.task.task_manage.models.entity

import jakarta.annotation.Generated
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="users")
data class Users(
    @Column(unique = true)
    val email:String ?=null,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long = 0L ,

    val hashedPassword:String
)
