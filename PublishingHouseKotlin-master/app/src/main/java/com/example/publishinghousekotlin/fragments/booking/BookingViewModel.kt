package com.example.publishinghousekotlin.fragments.booking


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.publishinghousekotlin.dtos.BookingSimpleAcceptDTO
import com.example.publishinghousekotlin.repositories.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


@HiltViewModel
class BookingViewModel : ViewModel() {

    data class SearchParameters(var bookingId: Long?, var status: String)


    private val searchParameters = MutableLiveData<SearchParameters>(SearchParameters(null, ""))


    val listOfBookings: LiveData<PagingData<BookingSimpleAcceptDTO>> = searchParameters.switchMap { searchParameters ->
        BookingRepository().getPagedBookings(searchParameters.bookingId, searchParameters.status).cachedIn(viewModelScope)
    }


    fun updateSearchParameters(bookingId: Long?, status: String){
        val params = SearchParameters(bookingId, status)
        searchParameters.value = params
    }


}