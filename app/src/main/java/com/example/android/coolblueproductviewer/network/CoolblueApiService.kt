package com.example.android.coolblueproductviewer.network

import com.example.android.coolblueproductviewer.entities.ProductsPayload
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class CoolblueApiService {

    private val gson = GsonConverterFactory.create()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(gson)
        .baseUrl(BASE_URL)
        .build()

    private var filter: String? = null
    private val retrofitService: CoolblueApi by lazy { retrofit.create(CoolblueApi::class.java) }

    suspend fun getProductsFiltered(page: Int): ProductsPayload {
        Timber.d("getProductsFiltered: filter: $filter, page: $page")
        return retrofitService.getProducts(filter = filter, page = page)
    }

    fun updateFilter(filter: String?) {
        this.filter = filter.takeUnless { filter?.isBlank() == true }
    }

    companion object {

        private const val BASE_URL = "https://bdk0sta2n0.execute-api.eu-west-1.amazonaws.com/"
    }
}
