package com.example.android.coolblueproductviewer.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.example.android.coolblueproductviewer.entities.Product
import timber.log.Timber
import java.io.IOException

class CoolblueProductsPagingSource(private val networkService: CoolblueApiService) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val currentPageNumber = params.key ?: 1
            val response = networkService.getProductsFiltered(page = currentPageNumber)

            val prevKey = if (response.currentPage > 1) {
                response.currentPage - 1
            } else null
            val nextKey = if (response.currentPage < response.pageCount) {
                response.currentPage + 1
            } else null

            return LoadResult.Page(
                data = response.products,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            Timber.d(e.message.toString())
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            Timber.d(e.message.toString())
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}
