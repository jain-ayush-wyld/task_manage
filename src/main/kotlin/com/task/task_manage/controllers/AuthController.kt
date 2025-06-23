package com.task.task_manage.controllers

import com.task.task_manage.service.AuthService
import com.task.task_manage.service.AuthService.TokenPair
import io.jsonwebtoken.security.Password
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController (
    private val authService: AuthService
){
    data class AuthRequest(
        val email : String,
        val password: String
    )
    @PostMapping("/register")
    fun register(
        @RequestBody body: AuthRequest ) : ResponseEntity<Void>
    {
        authService.register(body.email,body.password)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: AuthRequest ): ResponseEntity<TokenPair>
    {
        val token: AuthService.TokenPair = authService.login(body.email,body.password)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(token)

    }

}