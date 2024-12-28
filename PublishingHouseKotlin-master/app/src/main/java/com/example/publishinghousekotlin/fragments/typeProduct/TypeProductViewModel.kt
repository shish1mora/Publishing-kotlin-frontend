package com.example.publishinghousekotlin.fragments.typeProduct


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.publishinghousekotlin.models.TypeProduct
import com.example.publishinghousekotlin.repositories.TypeProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel



@HiltViewModel

/**
 * ViewModel для управления списком типов продукции в рамках фрагмента.
 *
 * Содержит LiveData [listOfTypeProducts], которая предоставляет пагинированный список типов продукции,
 * отфильтрованный по поисковому запросу [searchType].
 *
 * @property searchType LiveData, содержащая текущий поисковый запрос.
 * @property listOfTypeProducts LiveData, предоставляющая пагинированный список типов продукции.
 */
class TypeProductViewModel : ViewModel() {

    private val searchType = MutableLiveData<String>("")

    val listOfTypeProducts: LiveData<PagingData<TypeProduct>> = searchType.switchMap { query ->
        TypeProductRepository().getPagedTypeProducts(query).cachedIn(viewModelScope)
    }

    /**
     * Метод обновления searchType
     * @param query Новое значение searchType
     */
    fun updateSearchType(query: String) {
        searchType.value = query
    }

    //var list = TypeProductRepository().getPagedTypeProducts(currentQuery).cachedIn(viewModelScope)

//    private val _originalList = TypeProductRepository().getPagedTypeProducts().cachedIn(viewModelScope)
//    private val _list = MediatorLiveData<PagingData<TypeProduct>>()
//    val list: LiveData<PagingData<TypeProduct>> get() = _list
//
//    init {
//        // Подключение оригинального источника данных
//        _list.addSource(_originalList) {
//            _list.value = it
//        }
//    }


}