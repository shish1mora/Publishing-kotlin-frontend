package com.example.publishinghousekotlin.fragments.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.publishinghousekotlin.dtos.EmployeeDTO
import com.example.publishinghousekotlin.repositories.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel

/**
 * ViewModel для управления списком сотрудников в рамках фрагмента.
 *
 * Содержит LiveData [listOfEmployees], которая предоставляет пагинированный список сотрудников,
 * отфильтрованный по поисковому запросу [searchType].
 *
 * @property searchType LiveData, содержащая текущий поисковый запрос.
 * @property listOfEmployees LiveData, предоставляющая пагинированный список сотрудников.
 */
class EmployeeViewModel: ViewModel() {

    private val searchType = MutableLiveData<String>("")

    val listOfEmployees: LiveData<PagingData<EmployeeDTO>> = searchType.switchMap { query ->
        EmployeeRepository().getPagedEmployees(query).cachedIn(viewModelScope)
    }

    /**
     * Метод обновления searchType
     * @param query Новое значение searchType
     */
    fun updateSearchType(query: String){
        searchType.value = query
    }
}