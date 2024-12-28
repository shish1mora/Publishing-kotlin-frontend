package com.example.publishinghousekotlin.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.publishinghousekotlin.models.Material
import com.example.publishinghousekotlin.repositories.MaterialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Класс, представляющий источник данных для загрузки списка материалов с использованием пагинации.
 * @author Денис
 * @since 1.0.0
 * @param type Тип материала, по которому осуществляется фильтрация результатов.
 */
class MaterialsDataSource(private var type: String): PagingSource<Int, Material>() {

    /**
     * Метод для получения ключа обновления при использовании функционала пагинации.
     *
     * @param state Состояние текущей загрузки данных.
     * @return Ключ обновления или null, если его нет.
     */
    override fun getRefreshKey(state: PagingState<Int, Material>): Int? {
        val anchorPosition = state.anchorPosition?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    /**
     * Метод для загрузки данных в асинхронном режиме с использованием пагинации.
     *
     * @param params Параметры загрузки, такие как размер страницы и ключ пагинации.
     * @return Результат загрузки данных, содержащий список материалов и информацию о ключах пагинации.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Material> {
        val pageIndex = params.key ?: 0

        return try{
            val materials = withContext(Dispatchers.IO){
                MaterialRepository().get(pageIndex,type)
            }

            return LoadResult.Page(
                data = materials!!,
                prevKey = if(pageIndex == 0) null else pageIndex - 1,
                nextKey = if(materials.isNotEmpty()) pageIndex + 1 else null
            )
        } catch (e:Exception){
            LoadResult.Error(throwable = e)
        }
    }
}