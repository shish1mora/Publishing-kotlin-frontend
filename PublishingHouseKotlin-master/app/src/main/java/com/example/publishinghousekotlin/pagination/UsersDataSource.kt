package com.example.publishinghousekotlin.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.publishinghousekotlin.models.User
import com.example.publishinghousekotlin.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersDataSource(private var role: String, private var name: String): PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        val anchorPosition = state.anchorPosition?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val pageIndex = params.key ?: 0

        return try{
            val users = withContext(Dispatchers.IO){
                UserRepository().get(pageIndex, role, name)
            }

            return LoadResult.Page(
                data = users!!,
                prevKey = if(pageIndex == 0) null else pageIndex - 1,
                nextKey = if(users.isNotEmpty()) pageIndex + 1 else null
            )
        } catch (e:Exception){
            LoadResult.Error(throwable = e)
        }
    }
}