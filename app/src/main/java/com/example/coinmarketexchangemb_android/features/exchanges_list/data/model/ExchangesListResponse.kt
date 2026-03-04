package com.example.coinmarketexchangemb_android.features.exchanges_list.data.model

import com.google.gson.annotations.SerializedName

data class ExchangesListResponse(
    val data: List<Exchange>
)

data class Exchange(
    val id: Int,
    val name: String,
    val logo: String?,

    @SerializedName("quote")
    val quote: Quote? = null,

    @SerializedName("last_updated")
    val dateLaunched: String? = null
) {
    val spotVolumeUsd: Double
        get() = quote?.usd?.volume24h ?: 0.0
}

data class Quote(
    @SerializedName("USD")
    val usd: UsdQuote?
)

data class UsdQuote(
    @SerializedName("volume_24h")
    val volume24h: Double?
)