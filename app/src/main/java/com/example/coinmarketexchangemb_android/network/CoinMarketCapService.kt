package com.example.coinmarketexchangemb_android.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinMarketCapService {
    @GET("v1/exchange/listings/latest")
    suspend fun getExchanges(
        @Query("limit") limit: Int = 100
    ): Response<Unit>
}