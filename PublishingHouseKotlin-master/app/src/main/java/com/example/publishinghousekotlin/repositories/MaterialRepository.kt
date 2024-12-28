package com.example.publishinghousekotlin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.publishinghousekotlin.MyApplication
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.http.JwtInterceptor
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.models.Material
import com.example.publishinghousekotlin.pagination.MaterialsDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Репозиторий, предоставляющий методы для работы с данными материалов на сервере.
 * @author Денис
 * @since 1.0.0
 * @property client OkHttpClient
 * @property gson Поле, для сериализации и десериализации объектов Kotlin в JSON
 * @property apiUrl URL-адрес сервера, используемый для взаимодействия с API материалов.
 */
class MaterialRepository {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/materials"


    /**
     * Метод получения списка материалов с сервера с использованием пагинации и фильтрации по типу.
     *
     * @param page Номер страницы для пагинации.
     * @param type Тип материала для фильтрации результатов.
     * @return Список сотрудников или null, если произошла ошибка.
     */
    suspend fun get(page: Int?, type:String):List<Material>?{
        var partUrl = ""

        if(page != null) {
            partUrl = "?page=$page"
            if (type != "") {
                partUrl += "&type=$type"
            }
        }

        val request = Request.Builder()
            .url(apiUrl+partUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<Material>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    /**
     * Метод получения списка материалов с использованием пагинации.
     *
     * @param type Тип материала для фильтрации результатов.
     * @return LiveData содержащая список материалов с использованием пагинации.
     */
    fun getPagedMaterials(type:String) = Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = {MaterialsDataSource(type)}
    ).liveData


    /**
     * Метод удаления материала по его идентификатору на сервере.
     *
     * @param materialId Идентификатор материала.
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
    suspend fun delete(materialId:Long): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$materialId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 409 || response.code == 204){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    /**
     * Метод добавления нового материала на сервер.
     *
     * @param material Данные о материале.
     * @return Код состояния.
     */
    suspend fun add(material:Material): Int{
        val materialAsJson = gson.toJson(material)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = materialAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/add")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }


    /**
     * Метод обновления данных о материале на сервере.
     *
     * @param material Данные о материале.
     * @return Код состояния.
     */
    suspend fun update(material: Material): Int{
        val materialAsJson = gson.toJson(material)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = materialAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update/${material.id}")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }
}