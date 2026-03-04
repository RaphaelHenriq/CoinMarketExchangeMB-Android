package com.example.coinmarketexchangemb_android.network

import com.example.coinmarketexchangemb_android.features.exchanges_list.data.model.ExchangesListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinMarketCapService {
    @GET("v1/exchange/listings/latest")
    suspend fun getExchangesList(
        @Query("limit") limit: Int = 20
    ): Response<ExchangesListResponse>
}