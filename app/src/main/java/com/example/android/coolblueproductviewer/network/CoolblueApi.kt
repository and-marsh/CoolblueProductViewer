package com.example.android.coolblueproductviewer.network

import com.example.android.coolblueproductviewer.entities.ProductsPayload
import retrofit2.http.GET
import retrofit2.http.Query

interface CoolblueApi {

    @GET("mobile-assignment/search")
    suspend fun getProducts(
        @Query("query") filter: String? = null,
        @Query("page") page: Int
    ): ProductsPayload
}
