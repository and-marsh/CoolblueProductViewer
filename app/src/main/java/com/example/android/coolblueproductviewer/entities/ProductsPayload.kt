package com.example.android.coolblueproductviewer.entities

data class ProductsPayload(
    val products: List<Product> = listOf(),
    val currentPage: Int,
    val pageSize: Int,
    val totalResults: Int,
    val pageCount: Int
)
