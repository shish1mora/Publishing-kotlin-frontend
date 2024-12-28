package com.example.publishinghousekotlin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.publishinghousekotlin.MyApplication
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.http.JwtInterceptor
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.models.TypeProduct
import com.example.publishinghousekotlin.pagination.TypeProductsDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class TypeProductRepository {

    /**
     * Репозиторий, предоставляющий методы для работы с данными типов продукции на сервере.
     * @author Денис
     * @since 1.0.0
     * @property client OkHttpClient
     * @property gson Поле, для сериализации и десериализации объектов Kotlin в JSON
     * @property apiUrl URL-адрес сервера, используемый для взаимодействия с API типов продукции.
     */
    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/typeProducts"


    /**
     * Метод получения списка типов продукции с сервера с использованием пагинации и фильтрации по типу.
     *
     * @param page Номер страницы для пагинации.
     * @param type Тип для фильтрации результатов.
     * @return Список типов продукции или null, если произошла ошибка.
     */
    suspend fun get(page:Int?,type:String):List<TypeProduct>?{
        var partUrl = ""

        if(page != null) {
            partUrl = "?page=$page"
            if (type != "") {
                partUrl += "&type=$type"
            }
        }

        val request = Request.Builder()
            .url(apiUrl + partUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<TypeProduct>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null;
    }

    /**
     * Метод получения списка типов продукции с использованием пагинации.
     *
     * @param type Тип для фильтрации результатов.
     * @return LiveData содержащая список типов продукции с использованием пагинации.
     */
    fun getPagedTypeProducts(type:String) = Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = {TypeProductsDataSource(type)}
    ).liveData


    /**
     * Метод добавления нового типа продукции на сервер.
     *
     * @param typeProduct Данные о типе продукции.
     * @return Код состояния.
     */
    suspend fun add(typeProduct: TypeProduct): Int{

        val typeProductAsJson = gson.toJson(typeProduct)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = typeProductAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/add")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }

    /**
     * Метод обновления данных о типе продукции на сервере.
     *
     * @param typeProduct Данные о типе продукции.
     * @return Код состояния.
     */
    suspend fun update(typeProduct: TypeProduct): Int{
        val typeProductAsJson = gson.toJson(typeProduct)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = typeProductAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update/${typeProduct.id}")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        return response.code
    }

    /**
     * Метод удаления типа продукции по его идентификатору на сервере.
     *
     * @param typeProductId Идентификатор типа продукции.
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
    suspend fun delete(typeProductId: Long): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$typeProductId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 409 || response.code == 204){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }
}