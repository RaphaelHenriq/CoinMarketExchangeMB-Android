package com.example.coinmarketexchangemb_android.navigation

import kotlinx.serialization.Serializable

@Serializable
object ExchangesListRoute

@Serializable
data class ExchangeDetailsRoute(val id: Int)