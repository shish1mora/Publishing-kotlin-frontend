package com.example.publishinghousekotlin.fragments.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.publishinghousekotlin.models.User
import com.example.publishinghousekotlin.models.UserRole
import com.example.publishinghousekotlin.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CustomerViewModel: ViewModel() {

    private val searchType = MutableLiveData<String>("")

    val listOfCustomers: LiveData<PagingData<User>> = searchType.switchMap { query ->
        UserRepository().getPagedUsers(UserRole.CUSTOMER.name, query).cachedIn(viewModelScope)
    }

    fun updateSearchType(query: String){
        searchType.value = query
    }
}