package com.task.task_manage.controllers

import  com.task.task_manage.controllers.TaskController.TaskResponse
import com.task.task_manage.models.entity.Tasks
import com.task.task_manage.repository.TaskRepository
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Instant



@RestController
@RequestMapping("/tasks")
class TaskController (
    private val repository: TaskRepository
){
    data class TaskRequest(
        val id: Long ?,    //when user requests may be to edit or create new one so a ?
        val title:String,
        val description: String,
        val color: Long,
        val ownerId: Long
    )
    data class TaskResponse(    // as request exists user might need info. about an already added task or so
        val id: Long ?,
        val title:String ?,
        val description: String?,
        val color: Long?,
        val createdAt: Instant?
    )
    @PostMapping
    fun save(
        @RequestBody body: TaskRequest)  : ResponseEntity<TaskResponse> //body is a fnc. parameter by name just
    {
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val numOwnerId:Long = ownerId.toLong()
        val task  = repository.save(        //this will return back contents after adding into db
            Tasks(
                title = body.title,
                description = body.description,
                color = body.color,
//                id = body.id ?.let{(it)}?:((repository.findMaxId()?:0L)+1),      // no need inside Tasks data class value is given
                createdAt = Instant.now(),
                ownerId = numOwnerId
//                ownerId = ObjectId()
            )

        )
//        val task: Tasks = Tasks()       //initialize data class of type Transaction
//        task.title = body.title
//        task.description = body.description
//        task.color = body.color
//        task.id = body.id
//        task.createdAt = Date
//        task.ownerId = body.ownerId
//        val returSaved:Tasks=repository.save(task)

//        return returSaved.toResponse()
//            return task.toResponse()
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(task.toResponse())
    }
//    return

    @GetMapping
    fun findByOwnerId (): ResponseEntity< List<TaskResponse>>
    { val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val numOwnerId:Long = ownerId.toLong()
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(repository.findByOwnerId(numOwnerId).map {
            it.toResponse() } )
    }
//                (
//        @RequestParam(required = true) ownerId: String
//    ):


    @DeleteMapping(path=["/{id}"])
    fun deleteById(@PathVariable id: Long ): ResponseEntity<Void>
    {
        //            IllegalArgumentException("task not exist")
        val note = repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Task not exist.")
        }
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val numOwnerId:Long = ownerId.toLong()
        if (numOwnerId == note.ownerId)
        {
            repository.deleteById(id)
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build()
        }
        else{
//            throw IllegalArgumentException("not the owner")
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.")
        }


    }
}

private fun Tasks.toResponse() : TaskResponse         //called as a mapper function
{
    return TaskResponse(
        id = id,
        title = title,
        description = description,
        color = color,
        createdAt = createdAt
    )
}