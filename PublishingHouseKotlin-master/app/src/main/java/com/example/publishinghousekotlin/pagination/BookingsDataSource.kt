package com.example.publishinghousekotlin.pagination


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.publishinghousekotlin.dtos.BookingSimpleAcceptDTO
import com.example.publishinghousekotlin.repositories.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingsDataSource(private var bookingId: Long?, private var status:String):PagingSource<Int, BookingSimpleAcceptDTO>() {
    override fun getRefreshKey(state: PagingState<Int, BookingSimpleAcceptDTO>): Int? {
        val anchorPosition = state.anchorPosition?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookingSimpleAcceptDTO> {
        val pageIndex = params.key ?: 0

        return try{
            val bookings = withContext(Dispatchers.IO){
                BookingRepository().get(pageIndex, bookingId, status)
            }

            return LoadResult.Page(
                data = bookings!!,
                prevKey = if(pageIndex == 0) null else pageIndex - 1,
                nextKey = if(bookings.isNotEmpty()) pageIndex + 1 else null
            )
        }catch (e:Exception){
            LoadResult.Error(throwable = e)
        }
    }
}