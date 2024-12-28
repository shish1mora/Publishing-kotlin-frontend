package com.example.publishinghousekotlin.repositories
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.publishinghousekotlin.MyApplication
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.dtos.ProductAcceptDTO
import com.example.publishinghousekotlin.dtos.UserAcceptDTO
import com.example.publishinghousekotlin.http.JwtInterceptor
import com.example.publishinghousekotlin.http.requests.LoginRequest
import com.example.publishinghousekotlin.http.requests.RegisterRequest
import com.example.publishinghousekotlin.http.responses.JwtResponse
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.models.Material
import com.example.publishinghousekotlin.models.User
import com.example.publishinghousekotlin.pagination.MaterialsDataSource
import com.example.publishinghousekotlin.pagination.UsersDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


/**
 * Репозиторий, предоставляющий методы для работы с данными пользователей на сервере.
 * @author Денис
 * @since 1.0.0
 * @property client OkHttpClient
 * @property gson Поле, для сериализации и десериализации объектов Kotlin в JSON
 * @property apiUrlAuth URL-адрес сервера, используемый для взаимодействия с API авторизации и регистрации.
 * @property apiUrlUsers URL-адрес сервера, используемый для взаимодействия с API пользователей.
 */
class UserRepository() {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    //private val client = OkHttpClient()
    private val gson = GsonBuilder().create()
    private val apiUrlAuth = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/auth"
    private val apiUrlUsers = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/users"


    /**
     * Метод авторизации пользователя
     * @param loginData Данные для авторизации
     * @return Данные об авторизированном пользователе или null
     */
    suspend fun login(loginData: LoginRequest): JwtResponse?{

        val loginDataAsJson = gson.toJson(loginData)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = loginDataAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrlAuth/login")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        if(response.isSuccessful){
            return gson.fromJson(response.body?.string(), JwtResponse::class.java)
        }

        return null
     }


    /**
     * Метод регистрации пользователя
     * @param registerData Данные для регистрации
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
    suspend fun register(registerData: RegisterRequest):MessageResponse?{

        val registerDataAsJson = gson.toJson(registerData)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = registerDataAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrlAuth/register")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        if(response.code == 200 || response.code == 400){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    suspend fun get(page:Int, role: String, name: String):List<User>?{
        var partUrl = "?page=$page&role=$role"

        if(name != ""){
            partUrl += "&name=$name"
        }

        val request = Request.Builder()
            .url(apiUrlUsers+partUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<User>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    fun getPagedUsers(role:String, name: String) = Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = {UsersDataSource(role, name)}
    ).liveData


    suspend fun get(userId: Long): UserAcceptDTO? {
        val request = Request.Builder()
            .url("$apiUrlUsers/$userId")
            .build()

        val response = client.newCall(request).execute()
        if(response.code == 200){
            return gson.fromJson(response.body?.string(), UserAcceptDTO::class.java)
        }

        return null
    }
}