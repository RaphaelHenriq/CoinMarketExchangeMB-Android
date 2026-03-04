package com.example.coinmarketexchangemb_android.features.exchanges_list.presentation

import com.example.coinmarketexchangemb_android.features.exchanges_list.data.Exchange
import com.example.coinmarketexchangemb_android.utils.Text
sealed class ExchangesListState {
    object Loading : ExchangesListState()
    data class Success(val list: List<Exchange>) : ExchangesListState()
    data class Error(val message: Text) : ExchangesListState()
}