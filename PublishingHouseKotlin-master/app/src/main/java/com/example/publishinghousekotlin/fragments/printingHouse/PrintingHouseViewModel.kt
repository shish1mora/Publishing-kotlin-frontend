package com.example.publishinghousekotlin.fragments.printingHouse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.publishinghousekotlin.models.PrintingHouse
import com.example.publishinghousekotlin.repositories.PrintingHouseRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel

/**
 * ViewModel для управления списком типографий в рамках фрагмента.
 *
 * Содержит LiveData [listOfPrintingHouses], которая предоставляет пагинированный список типографий,
 * отфильтрованный по поисковому запросу [searchType].
 *
 * @property searchType LiveData, содержащая текущий поисковый запрос.
 * @property listOfPrintingHouses LiveData, предоставляющая пагинированный список типографий.
 */
class PrintingHouseViewModel: ViewModel() {

    private val searchType = MutableLiveData<String>("")

    val listOfPrintingHouses: LiveData<PagingData<PrintingHouse>> = searchType.switchMap { query ->
        PrintingHouseRepository().getPagedPrintingHouses(query).cachedIn(viewModelScope)
    }

    /**
     * Метод обновления searchType
     * @param query Новое значение searchType
     */
    fun updateSearchType(query: String){
        searchType.value = query
    }
}