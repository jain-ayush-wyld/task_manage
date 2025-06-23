package com.task.task_manage.service

import com.task.task_manage.models.entity.Users
import com.task.task_manage.repository.UserRepository
import com.task.task_manage.security.HashEncoder
import io.jsonwebtoken.security.Password
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService (
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
){
    data class TokenPair(
        val accessToken: String,
//        val refreshToken: String
    )
    fun register(email:String,password: String): Users
    {
//        val user = userRepository.findByEmail(email)
//            ?: throw BadCredentialsException("user already exists.")
//        println(user)
        val userFi = userRepository.findByEmail(email.trim())
        val user = Users(
            email = email,
            hashedPassword = hashEncoder.encode(password)
        )
        if (userFi!=null){
            throw ResponseStatusException(HttpStatus.CONFLICT, "A user with that email already exists.")
        }
//        return try {
//            userRepository.save(user)
//        } catch (ex: DataIntegrityViolationException) {
//            throw IllegalArgumentException("Email '$email' is already in use.")
//        }
        return userRepository.save(
            Users(
                email = email,
                hashedPassword = hashEncoder.encode(password)
            )
        )
    }

    fun login(email:String,password:String) : TokenPair
    {
        val user = userRepository.findByEmail(email)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.")
//            ?: throw BadCredentialsException("invalid credentials.")
//        println(user)
        if (!hashEncoder.matches(password,user.hashedPassword)){
//            throw BadCredentialsException("invalid credentials.")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.")
        }
//        println("yes")
        val token = TokenPair(
            accessToken = jwtService.generateAccessToken(user.id),
//            refreshToken = jwtService.generateRefreshToken(user.id)
        )
        return token
    }

}