package com.example.publishinghousekotlin.fragments.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.publishinghousekotlin.dtos.ProductAcceptDTO
import com.example.publishinghousekotlin.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel


/**
 * ViewModel для управления списком продукций в рамках фрагмента.
 *
 * Содержит LiveData [listOfProducts], которая предоставляет пагинированный список продукций,
 * отфильтрованный по поисковому запросу [searchType].
 *
 * @property searchType LiveData, содержащая текущий поисковый запрос.
 * @property listOfProducts LiveData, предоставляющая пагинированный список продукций.
 */
class ProductViewModel: ViewModel() {

    private val searchType = MutableLiveData<String>("")

    val listOfProducts: LiveData<PagingData<ProductAcceptDTO>> = searchType.switchMap { query ->
        ProductRepository().getPagedProducts(query).cachedIn(viewModelScope)
    }


    /**
     * Метод обновления searchType
     * @param query Новое значение searchType
     */
    fun updateSearchType(query: String){
        searchType.value = query
    }
}