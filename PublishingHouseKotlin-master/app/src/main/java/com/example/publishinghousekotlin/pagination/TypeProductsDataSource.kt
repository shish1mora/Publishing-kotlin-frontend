package com.example.publishinghousekotlin.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.publishinghousekotlin.models.TypeProduct
import com.example.publishinghousekotlin.repositories.TypeProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Класс, представляющий источник данных для загрузки списка типов продукции с использованием пагинации.
 * @author Денис
 * @since 1.0.0
 * @param type Тип продукции, по которому осуществляется фильтрация результатов.
 */
class TypeProductsDataSource(private var type:String): PagingSource<Int, TypeProduct>() {

    /**
     * Метод для получения ключа обновления при использовании функционала пагинации.
     *
     * @param state Состояние текущей загрузки данных.
     * @return Ключ обновления или null, если его нет.
     */
    override fun getRefreshKey(state: PagingState<Int, TypeProduct>): Int? {
        val anchorPosition = state.anchorPosition?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    /**
     * Метод для загрузки данных в асинхронном режиме с использованием пагинации.
     *
     * @param params Параметры загрузки, такие как размер страницы и ключ пагинации.
     * @return Результат загрузки данных, содержащий список типов продукции и информацию о ключах пагинации.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TypeProduct> {
        val pageIndex = params.key ?: 0

        return try{
                val typeProducts = withContext(Dispatchers.IO){
                    TypeProductRepository().get(pageIndex,type)
                }

            return LoadResult.Page(
                data = typeProducts!!,
                prevKey = if(pageIndex == 0) null else pageIndex - 1,
                //nextKey = if(typeProducts.size == params.loadSize) pageIndex + (params.loadSize / pageSize) else null
                nextKey = if(typeProducts.isNotEmpty()) pageIndex + 1 else null
            )
        } catch (e:Exception){
            LoadResult.Error(throwable = e)
        }
    }
}