package com.example.publishinghousekotlin.fragments.material

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.publishinghousekotlin.models.Material
import com.example.publishinghousekotlin.repositories.MaterialRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel

/**
 * ViewModel для управления списком материалов в рамках фрагмента.
 *
 * Содержит LiveData [listOfMaterials], которая предоставляет пагинированный список материалов,
 * отфильтрованный по поисковому запросу [searchType].
 *
 * @property searchType LiveData, содержащая текущий поисковый запрос.
 * @property listOfMaterials LiveData, предоставляющая пагинированный список материалов.
 */
class MaterialViewModel : ViewModel() {

    private val searchType = MutableLiveData<String>("")

    val listOfMaterials: LiveData<PagingData<Material>> = searchType.switchMap { query ->
        MaterialRepository().getPagedMaterials(query).cachedIn(viewModelScope)
    }

    /**
     * Метод обновления searchType
     * @param query Новое значение searchType
     */
    fun updateSearchType(query: String){
        searchType.value = query
    }
}