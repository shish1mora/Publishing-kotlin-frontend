package ru.karpov.publishing.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ru.karpov.publishing.MyApplication
import ru.karpov.publishing.R
import ru.karpov.publishing.adapters.LocalDateAdapter
import ru.karpov.publishing.dtos.BookingAcceptDTO
import ru.karpov.publishing.dtos.BookingSendDTO
import ru.karpov.publishing.dtos.BookingSimpleAcceptDTO
import ru.karpov.publishing.http.JwtInterceptor
import ru.karpov.publishing.http.requests.GenerateReportRequest
import ru.karpov.publishing.http.responses.JwtResponse
import ru.karpov.publishing.http.responses.MessageResponse
import ru.karpov.publishing.models.UserRole
import ru.karpov.publishing.pagination.BookingsDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDate

class BookingRepository {

    private val client = OkHttpClient.Builder().addInterceptor(JwtInterceptor()).build()
    private val gson = GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()
    private val apiUrl = MyApplication.instance.applicationContext.resources.getString(R.string.server) + "/api/bookings"


    suspend fun add(booking: BookingSendDTO): MessageResponse?{
        val bookingAsJson = gson.toJson(booking)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = bookingAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/add")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200 || response.code == 409){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }


    suspend fun get(page: Int, bookingId: Long?, status: String):List<BookingSimpleAcceptDTO>?{
        var partUrl = "?"

        if(bookingId != null){
            partUrl += "bookingId=$bookingId"
        }else{
            partUrl += "page=$page"
            val user = JwtResponse.getFromMemory(MyApplication.instance.applicationContext, MyApplication.instance.applicationContext.resources.getString(R.string.keyForJwtResponse))!!.user
            if(user.role == UserRole.CUSTOMER.name){
                partUrl +="&userId=${user.id}"
            }

            if(status != ""){
                partUrl+="&status=$status"
            }
        }

        val request = Request.Builder()
            .url(apiUrl + partUrl)
            .build()

        val response = client.newCall(request).execute()

        if(response.isSuccessful){
            val typeOfData = object : TypeToken<List<BookingSimpleAcceptDTO>>() {}.type

            return gson.fromJson(response.body?.string(), typeOfData)
        }

        return null
    }

    suspend fun get(bookingId: Long):BookingAcceptDTO?{
        val request = Request.Builder()
            .url("$apiUrl/$bookingId")
            .build()

        val response = client.newCall(request).execute()
        if(response.code == 200){
            return gson.fromJson(response.body?.string(), BookingAcceptDTO::class.java)
        }

        return null
    }

    fun getPagedBookings(bookingId:Long?,status:String)= Pager(
        config = PagingConfig(pageSize = 7, enablePlaceholders = false),
        pagingSourceFactory = { BookingsDataSource(bookingId, status)}
    ).liveData


    suspend fun delete(bookingId: Long):MessageResponse?{
        val request = Request.Builder()
            .url("$apiUrl/delete/$bookingId")
            .delete()
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 204 || response.code == 409){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }

    suspend fun update(booking: BookingSendDTO,updateStatus:String):MessageResponse?{
        val bookingAsJson = gson.toJson(booking)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = bookingAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/update/${booking.id}?updateStatus=$updateStatus")
            .put(body)
            .build()

        val response = client.newCall(request).execute()

        if(response.code == 200 || response.code == 400 || response.code == 409){
            return MessageResponse(response.code, response.body!!.string())
        }

        return null
    }


    suspend fun generateRepost(generateReportRequest: GenerateReportRequest): MessageResponse{
        val requestAsJson = gson.toJson(generateReportRequest)
        val mediaType = "application/json; chapset=utf-8".toMediaType()
        val body = requestAsJson.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$apiUrl/generateReport")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        return MessageResponse(response.code, response.body!!.string())
    }
}