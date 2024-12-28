package ru.karpov.publishing.http.requests


data class RegisterRequest(

    private val name:String,
    private val phone:String,
    private val email:String,
    private val password:String,
    private val role: String
)