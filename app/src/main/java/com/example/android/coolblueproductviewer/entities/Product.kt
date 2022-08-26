package com.example.android.coolblueproductviewer.entities

import com.google.gson.annotations.SerializedName

data class Product(
    val productId: Int? = null,
    val productName: String? = null,
    val reviewInformation: ReviewInformation,
    @SerializedName("USPs")
    var uspList: List<String> = listOf(),
    val availabilityState: Int? = null,
    val salesPriceIncVat: Double,
    val listPriceIncVat: Double? = null,
    val listPriceExVat: Double? = null,
    val productImage: String? = null,
    val coolbluesChoiceInformationTitle: String? = null,
    val promoIcon: PromoIcon? = PromoIcon(),
    val nextDayDelivery: Boolean? = null
)
