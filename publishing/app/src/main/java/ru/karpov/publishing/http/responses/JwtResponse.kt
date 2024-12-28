package ru.karpov.publishing.http.responses

import android.content.Context
import android.util.Base64
import ru.karpov.publishing.models.User
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


data class JwtResponse(

    val token:String,

    val type:String,

    val user: User

) : Serializable{
    companion object{


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


        fun deleteFromMemory(context: Context, key: String){
            val sharedPreferences = context.getSharedPreferences("Responses", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.remove(key)
            editor.apply()

        }
    }
}