package ru.karpov.publishing.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ru.karpov.publishing.MyApplication
import ru.karpov.publishing.R
import ru.karpov.publishing.http.JwtInterceptor
import ru.karpov.publishing.http.responses.MessageResponse
import ru.karpov.publishing.models.PrintingHouse
import ru.karpov.publishing.pagination.PrintingHousesDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


class PrintingHouseRepository {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/printingHouses"

 
    suspend fun get(page: Int?, name:String):List<PrintingHouse>?{
        var partUrl = ""

        if(page != null) {
            partUrl = "?page=$page"
            if (name != "") {
                partUrl += "&name=$name"
            }
        }

        val request = Request.Builder()
            .url(apiUrl+partUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<PrintingHouse>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }


    fun getPagedPrintingHouses(name:String) = Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = {PrintingHousesDataSource(name)}
    ).liveData

 
    suspend fun delete(printingHouseId:Long):MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$printingHouseId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 204 || response.code == 409){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }


    suspend fun update(printingHouse: PrintingHouse):MessageResponse?{
        val printingHouseAsJson = gson.toJson(printingHouse)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = printingHouseAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update/${printingHouse.id}")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200 || response.code == 400 || response.code == 409){
            if(response.code == 200){
                return MessageResponse(response.code, "Данные о типографии успешно изменены!")
            }
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    suspend fun add(printingHouse: PrintingHouse):MessageResponse?{
        val printingHouseAsJson = gson.toJson(printingHouse)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = printingHouseAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/add")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200 || response.code == 409){
            if(response.code == 200){
                return MessageResponse(response.code, "Типография успешно добавлена!")
            }
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }
}