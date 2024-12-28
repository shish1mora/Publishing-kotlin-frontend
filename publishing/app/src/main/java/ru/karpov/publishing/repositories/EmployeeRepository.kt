package ru.karpov.publishing.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ru.karpov.publishing.MyApplication
import ru.karpov.publishing.R
import ru.karpov.publishing.adapters.LocalDateAdapter
import ru.karpov.publishing.http.JwtInterceptor
import ru.karpov.publishing.http.responses.MessageResponse
import ru.karpov.publishing.models.Employee
import ru.karpov.publishing.dtos.EmployeeDTO
import ru.karpov.publishing.pagination.EmployeesDataSource
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


class EmployeeRepository {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/employees"



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


    fun getPagedEmployees(surname: String) = Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = {EmployeesDataSource(surname)}
    ).liveData



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