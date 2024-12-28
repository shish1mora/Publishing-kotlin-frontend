package ru.karpov.publishing.http.requests


data class LoginRequest(

    private val email:String,
    private val password: String
)