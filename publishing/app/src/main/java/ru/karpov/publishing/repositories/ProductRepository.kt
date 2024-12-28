package ru.karpov.publishing.repositories


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ru.karpov.publishing.MyApplication
import ru.karpov.publishing.R
import ru.karpov.publishing.dtos.ProductAcceptDTO
import ru.karpov.publishing.dtos.ProductSendDTO
import ru.karpov.publishing.http.JwtInterceptor
import ru.karpov.publishing.http.responses.JwtResponse
import ru.karpov.publishing.http.responses.MessageResponse
import ru.karpov.publishing.models.UserRole
import ru.karpov.publishing.pagination.ProductsDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File



class ProductRepository {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/products"

 
    suspend fun get(page: Int?, name: String): List<ProductAcceptDTO>?{
        var partUrl = ""

        if(page != null) {
            partUrl = "?page=$page"
        }

        if(name != ""){
            partUrl += "&name=$name"
        }

        val symbol = if(partUrl != "") "&" else "?"
        val user = JwtResponse.getFromMemory(MyApplication.instance.applicationContext, MyApplication.instance.applicationContext.resources.getString(R.string.keyForJwtResponse))!!.user
        if(user.role == UserRole.CUSTOMER.name){
            partUrl+= symbol +"userId=${user.id}"
        }

        val request = Request.Builder()
            .url(apiUrl + partUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<ProductAcceptDTO>>() {}.type

            return gson.fromJson(response.body?.string(), typeOfData)
        }

        return null
    }


    suspend fun get(productId:Long): ProductAcceptDTO?{

        val request = Request.Builder()
            .url("$apiUrl/$productId")
            .build()

        val response = client.newCall(request).execute()
        if(response.code == 200){
            return gson.fromJson(response.body?.string(), ProductAcceptDTO::class.java)
        }

        return null
    }

 
    fun getPagedProducts(name:String)= Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = { ProductsDataSource(name)}
    ).liveData


 
    suspend fun add(product: ProductSendDTO, photos: List<File>): MessageResponse? {
        val productAsJson = gson.toJson(product)

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("product", null, productAsJson.toRequestBody("application/json; chapset=utf-8".toMediaType()))


        for ((index, file) in photos.withIndex()) {
            val requestBody = file.asRequestBody("image/*".toMediaType())
            body.addFormDataPart("photos", "${index + 1}.png", requestBody)
        }

        val request = Request.Builder()
            .url("$apiUrl/add")
            .post(body.build())
            .build()

        val response = client.newCall(request).execute()
        if(response.code == 200 || response.code == 400){
            if(response.code == 200){
                return MessageResponse(response.code, "Продукция успешно добавлена!")
            }

            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

 
    suspend fun delete(productId:Long): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$productId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 204 || response.code == 409){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }


    suspend fun update(product: ProductSendDTO, photos: List<File>): MessageResponse?{
        val productAsJson = gson.toJson(product)

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("product", null, productAsJson.toRequestBody("application/json; chapset=utf-8".toMediaType()))


        for ((index, file) in photos.withIndex()) {
            val requestBody = file.asRequestBody("image/*".toMediaType())
            body.addFormDataPart("photos", "${index + 1}.png", requestBody)
        }

        val request = Request.Builder()
            .url("$apiUrl/update/${product.id}")
            .put(body.build())
            .build()

        val response = client.newCall(request).execute()
        if(response.code == 200 || response.code == 400 || response.code == 409){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }
}