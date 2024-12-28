package com.example.publishinghousekotlin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.publishinghousekotlin.MyApplication
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.http.JwtInterceptor
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.models.PrintingHouse
import com.example.publishinghousekotlin.pagination.PrintingHousesDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Репозиторий, предоставляющий методы для работы с данными типографий на сервере.
 * @author Денис
 * @since 1.0.0
 * @property client OkHttpClient
 * @property gson Поле, для сериализации и десериализации объектов Kotlin в JSON
 * @property apiUrl URL-адрес сервера, используемый для взаимодействия с API типографий.
 */
class PrintingHouseRepository {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/printingHouses"

    /**
     * Метод получения списка типографий с сервера с использованием пагинации и фильтрации по названию.
     *
     * @param page Номер страницы для пагинации.
     * @param name Название типографии для фильтрации результатов.
     * @return Список типографий или null, если произошла ошибка.
     */
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

    /**
     * Метод получения списка типографий с использованием пагинации.
     *
     * @param name Название типографии для фильтрации результатов.
     * @return LiveData содержащая список типографий с использованием пагинации.
     */
    fun getPagedPrintingHouses(name:String) = Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = {PrintingHousesDataSource(name)}
    ).liveData

    /**
     * Метод удаления типографии по её идентификатору на сервере.
     *
     * @param printingHouseId Идентификатор типографии.
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
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

    /**
     * Метод обновления данных о типографии на сервере.
     *
     * @param printingHouse Данные о типографии.
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
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

    /**
     * Метод добавления новой типографии на сервер.
     *
     * @param printingHouse Данные о типографии.
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
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