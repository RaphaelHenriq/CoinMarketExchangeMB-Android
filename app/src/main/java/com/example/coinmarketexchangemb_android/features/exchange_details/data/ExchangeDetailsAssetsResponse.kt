package com.example.coinmarketexchangemb_android.features.exchange_details.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeDetailsAssetsResponse(
    val data: Map<String, List<Asset>>
)

@Serializable
data class Asset(
    val currency: CurrencyInfo
)

@Serializable
data class CurrencyInfo(
    val name: String,
    val symbol: String,
    @SerialName("price_usd") val priceUsd: Double? = 0.0
)