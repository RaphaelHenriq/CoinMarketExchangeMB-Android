package com.example.coinmarketexchangemb_android.features.exchange_details.data

import com.example.coinmarketexchangemb_android.network.RetrofitClient
import retrofit2.Response

class ExchangeDetailsRepository {
    private val api = RetrofitClient.service

    suspend fun getExchangeMetadata(id: String): Response<ExchangeDetailsResponse> = api.getExchangeDetails(id)
}