package com.example.publishinghousekotlin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.publishinghousekotlin.MyApplication
import com.example.publishinghousekotlin.R
import com.example.publishinghousekotlin.adapters.LocalDateAdapter
import com.example.publishinghousekotlin.http.JwtInterceptor
import com.example.publishinghousekotlin.http.responses.MessageResponse
import com.example.publishinghousekotlin.models.Employee
import com.example.publishinghousekotlin.dtos.EmployeeDTO
import com.example.publishinghousekotlin.pagination.EmployeesDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDate

/**
 * Репозиторий, предоставляющий методы для работы с данными сотрудников на сервере.
 * @author Денис
 * @since 1.0.0
 * @property client OkHttpClient
 * @property gson Поле, для сериализации и десериализации объектов Kotlin в JSON
 * @property apiUrl URL-адрес сервера, используемый для взаимодействия с API сотрудников.
 */
class EmployeeRepository {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/employees"


    /**
     * Метод получения списка сотрудников с сервера с использованием пагинации и фильтрации по фамилии.
     *
     * @param page Номер страницы для пагинации.
     * @param surname Фамилия для фильтрации результатов.
     * @return Список сотрудников или null, если произошла ошибка.
     */
    suspend fun get(page:Int?, surname:String):List<EmployeeDTO>?{
        var partUrl = ""

        if(page != null) {
            partUrl = "?page=$page"
            if (surname != "") {
                partUrl += "&surname=$surname"
            }
        }

        val request = Request.Builder()
            .url(apiUrl + partUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<EmployeeDTO>>() {}.type
            return gson.fromJson(response.body?.string(), typeOfData);
        }

        return null
    }

    /**
     * Метод получения списка сотрудников с использованием пагинации.
     *
     * @param surname Фамилия для фильтрации результатов.
     * @return LiveData содержащая список сотрудников с использованием пагинации.
     */
    fun getPagedEmployees(surname: String) = Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = {EmployeesDataSource(surname)}
    ).liveData


    /**
     * Метод добавления нового сотрудника на сервер.
     *
     * @param employee Данные о сотруднике.
     * @param photo Фотография сотрудника.
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
    suspend fun add(employee: Employee, photo: File):MessageResponse?{
        val employeeAsJson = gson.toJson(employee)

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("employee", null, employeeAsJson.toRequestBody("application/json; chapset=utf-8".toMediaType()))
            .addFormDataPart("photo", photo.name, photo.asRequestBody("image/*".toMediaType()))
            .build()

        val request = Request.Builder()
            .url("$apiUrl/add")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        if(response.code == 200 || response.code == 400 || response.code == 409){
            if(response.code == 200){
                return MessageResponse(response.code, "Сотрудник успешно добавлен!")
            }
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    /**
     * Метод удаления сотрудника по его идентификатору на сервере.
     *
     * @param employeeId Идентификатор сотрудника.
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
    suspend fun delete(employeeId: Long): MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$employeeId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 204 || response.code == 409){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    /**
     * Метод обновления данных о сотруднике на сервере.
     *
     * @param employee Данные о сотруднике.
     * @param photo Фотография сотрудника.
     * @return Объект [MessageResponse] с информацией о результате операции.
     */
    suspend fun update(employee: Employee, photo: File): MessageResponse?{
        val employeeAsJson = gson.toJson(employee)

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("employee", null, employeeAsJson.toRequestBody("application/json; chapset=utf-8".toMediaType()))
            .addFormDataPart("photo", photo.name, photo.asRequestBody("image/*".toMediaType()))
            .build()

        val request = Request.Builder()
            .url("$apiUrl/update/${employee.id}")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200 || response.code == 400 || response.code == 409){
            if(response.code == 200){
                return MessageResponse(response.code, "Данные о сотруднике успешно изменены!")
            }
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }
}