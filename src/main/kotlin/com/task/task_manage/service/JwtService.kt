package com.task.task_manage.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Date
import kotlin.io.encoding.Base64

@Service            //still treated as a usual class only
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String
) {
//    val jwtSecret:String= "ayushjain"
    private val secretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
//    lateinit var SECRET : String
    private val accessTokenValidityMs: Long = 100 * 60 * 1000        //10 minutes
//    private val refreshTokenValidityMs: Long = 30L * 24 * 60 * 60 * 1000L
//    private val secretKey = jwtSecret.toByteArray()

    private fun generateToken(userId: Long, type: String, expiry: Long): String
    {
//        println(userId)
//        println("generate token")
        val now = Date()
        val expiryTime = Date(now.time + expiry )
        return Jwts.builder()
            .subject(userId.toString())
            .claim("type",type)
            .issuedAt(now)
            .expiration(expiryTime)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }
    fun generateAccessToken(userId: Long):String{
        return generateToken(userId,"access",accessTokenValidityMs)
    }
//    fun generateRefreshToken(userId: Long):String{
//        return generateToken(userId,"refresh",refreshTokenValidityMs)
//    }
    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "access"
    }

//    fun validateRefreshToken(token: String): Boolean {
//        val claims = parseAllClaims(token) ?: return false
//        val tokenType = claims["type"] as? String ?: return false
//        return tokenType == "refresh"
//    }

    fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
            "Invalid token."
        )
//        IllegalArgumentException("invalid token")
//        println(claims.subject)

        return claims.subject       //returns Long userId in a string way, REM. to convert later on use
    }

//    private fun parseAllClaims(token: String): Claims? {
//        val rawToken = if(token.startsWith("Bearer ")) {
//            token.removePrefix("Bearer ")
//        } else token
//        return try {
//            Jwts.parser()
//                .verifyWith(secretKey)
//                .build()
//                .parseSignedClaims(rawToken)
//                .payload
//        } catch(e: Exception) {
//            null
//        }
//    }
private fun parseAllClaims(token: String): Claims? {
        val rawToken = token.removePrefix("Bearer ").trim()
        return try {
            Jwts.parser()  // modern parser
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload  // extract the Claims
        } catch (e: Exception) {
            null
        }
    }
}
