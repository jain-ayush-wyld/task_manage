package com.task.task_manage.models.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Date
import java.sql.Time
import java.time.Instant

@Entity
@Table(name="tasks")
data class Tasks (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long ? = null,
    var title: String ? = null,
    var description: String ? = null,
    var ownerId: Long ? = null,
    var color:Long ? = null,
    var createdAt: Instant?= null
)
/////////       below if still keeping Tasks as a open class
//@Id
//@GeneratedValue(strategy = GenerationType.AUTO)
//var id:Long ? = null
//var title: String ? = null
//var description: String ? = null
//var ownerId:String ? = null
//var color:Long ? = null
//var createdAt: Date ?= null
