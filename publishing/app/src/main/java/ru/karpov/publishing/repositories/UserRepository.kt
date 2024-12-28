package ru.karpov.publishing.repositories
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ru.karpov.publishing.MyApplication
import ru.karpov.publishing.R
import ru.karpov.publishing.dtos.ProductAcceptDTO
import ru.karpov.publishing.dtos.UserAcceptDTO
import ru.karpov.publishing.http.JwtInterceptor
import ru.karpov.publishing.http.requests.LoginRequest
import ru.karpov.publishing.http.requests.RegisterRequest
import ru.karpov.publishing.http.responses.JwtResponse
import ru.karpov.publishing.http.responses.MessageResponse
import ru.karpov.publishing.models.Material
import ru.karpov.publishing.models.User
import ru.karpov.publishing.pagination.MaterialsDataSource
import ru.karpov.publishing.pagination.UsersDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody



class UserRepository() {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().create()
    private val apiUrlAuth = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/auth"
    private val apiUrlUsers = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/users"


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