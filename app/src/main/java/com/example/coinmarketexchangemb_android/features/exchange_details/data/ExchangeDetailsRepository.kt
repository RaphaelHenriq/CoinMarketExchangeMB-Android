package com.example.coinmarketexchangemb_android.features.exchange_details.data

import com.example.coinmarketexchangemb_android.network.RetrofitClient
import retrofit2.Response

class ExchangeDetailsRepository {
    private val api = RetrofitClient.service

    suspend fun getExchangeDetails(id: String): Response<ExchangeDetailsResponse> = api.getExchangeDetails(id)

    suspend fun getExchangeDetailsAssets(id: String): Response<ExchangeDetailsAssetsResponse> = api.getExchangeDetailsAssets(id)
}