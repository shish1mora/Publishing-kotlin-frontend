package ru.karpov.publishing.models

import java.io.Serializable


data class User(

    val id: Long,
    val name:String,
    val phone: String,
    val email: String,
    val role: String

): Serializable{
    companion object{
        private const val serialVersionUID = 528283900914260698L
    }
}