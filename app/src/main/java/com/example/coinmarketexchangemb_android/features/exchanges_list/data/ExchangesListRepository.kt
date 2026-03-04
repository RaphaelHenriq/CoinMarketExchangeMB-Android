package com.example.coinmarketexchangemb_android.features.exchanges_list.data

import com.example.coinmarketexchangemb_android.network.RetrofitClient

class ExchangesListRepository {
    private val api = RetrofitClient.service

    suspend fun fetchExchangesList() = api.getExchangesList(limit = 20)
}