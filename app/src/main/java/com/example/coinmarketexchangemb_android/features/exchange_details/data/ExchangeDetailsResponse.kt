package com.example.coinmarketexchangemb_android.features.exchange_details.data

import com.google.gson.annotations.SerializedName

data class ExchangeDetailsResponse(
    val data: Map<String, ExchangeDetails>
)

data class ExchangeDetails(
    val id: Int,
    val name: String,
    val logo: String,
    val description: String?,
    val urls: ExchangeUrls?,
    @SerializedName("maker_fee") val makerFee: Double?,
    @SerializedName("taker_fee") val takerFee: Double?,
    @SerializedName("date_launched") val dateLaunched: String?,
    val assets: List<ExchangeAsset>? = emptyList()
)

data class ExchangeUrls(
    val website: List<String>?
)

data class ExchangeAsset(
    val name: String,
    @SerializedName("price_usd") val priceUsd: Double
)
