package com.example.coinmarketexchangemb_android.features.exchange_details.presentation

import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetails
import com.example.coinmarketexchangemb_android.utils.Text

sealed class ExchangeDetailsState {
    object Loading : ExchangeDetailsState()
    data class Success(val exchangeDetails: ExchangeDetails) : ExchangeDetailsState()
    data class Error(val message: Text) : ExchangeDetailsState()
}