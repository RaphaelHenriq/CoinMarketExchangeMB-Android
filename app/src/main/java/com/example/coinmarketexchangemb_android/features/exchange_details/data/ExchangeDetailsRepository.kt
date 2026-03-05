package com.example.coinmarketexchangemb_android.features.exchange_details.data

import com.example.coinmarketexchangemb_android.network.CoinMarketCapService
import com.example.coinmarketexchangemb_android.network.RetrofitClient
import retrofit2.Response

class ExchangeDetailsRepository(
    private val api: CoinMarketCapService = RetrofitClient.service
) {
    suspend fun fetchExchangeDetails(id: String): Response<ExchangeDetailsResponse> = api.getExchangeDetails(id)
    suspend fun fetchExchangeDetailsAssets(id: String): Response<ExchangeDetailsAssetsResponse> = api.getExchangeDetailsAssets(id)
}