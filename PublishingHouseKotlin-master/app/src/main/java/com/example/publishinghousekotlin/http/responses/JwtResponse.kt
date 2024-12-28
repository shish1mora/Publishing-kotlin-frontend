package com.example.publishinghousekotlin.http.responses

import android.content.Context
import android.util.Base64
import com.example.publishinghousekotlin.models.User
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Класс для работы с данными авторизованного пользователя
 * @author Климачков Даниил
 * @since 1.0.0
 * @property token JWT токен.
 * @property type Тип токена.
 * @property user Данные о пользователе
 */
data class JwtResponse(

    val token:String,

    val type:String,

    val user: User

) : Serializable{
    companion object{

        /**
         * Метод для сохранения объекта [JwtResponse] в память устройства по ключу [key].
         *
         * @param context Контекст приложения.
         * @param jwtResponse Объект [JwtResponse], который необходимо сохранить в память.
         * @param key Ключ, по которому будет производиться сохранение.
         */
        fun saveToMemory(context: Context, jwtResponse: JwtResponse, key:String){

            val sharedPreferences = context.getSharedPreferences("Responses", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            val byteArrayOutputStream = ByteArrayOutputStream()
            val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
            objectOutputStream.writeObject(jwtResponse)

            val objectBytes = byteArrayOutputStream.toByteArray()
            editor.putString(key, Base64.encodeToString(objectBytes, Base64.DEFAULT))
            editor.apply()
        }

        /**
         * Метод для получения объекта [JwtResponse] из памяти устройства по ключу [key].
         *
         * @param context Контекст приложения.
         * @param key Ключ, по которому будет производиться поиск объекта.
         *
         * @return Объект [JwtResponse] или null, если объект не найден.
         */
        fun getFromMemory(context: Context, key: String): JwtResponse?{

            val sharedPreferences = context.getSharedPreferences("Responses", Context.MODE_PRIVATE)
            val jwtResponseAsString = sharedPreferences.getString(key,null)

            if(jwtResponseAsString != null){
                val objectBytes = Base64.decode(jwtResponseAsString, Base64.DEFAULT)
                val byteArrayInputStream = ByteArrayInputStream(objectBytes)
                val objectInputStream = ObjectInputStream(byteArrayInputStream)

                return objectInputStream.readObject() as? JwtResponse
            }

            return null
        }

        /**
         * Метод для удаления объекта из памяти устройства по ключу [key].
         *
         * @param context Контекст приложения.
         * @param key Ключ, по которому будет производиться удаление объекта.
         */
        fun deleteFromMemory(context: Context, key: String){
            val sharedPreferences = context.getSharedPreferences("Responses", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.remove(key)
            editor.apply()

        }
    }
}