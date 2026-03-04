package com.example.coinmarketexchangemb_android.utils

import java.text.NumberFormat
import java.util.Locale

fun Double?.toCurrency(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(this ?: 0.0)
}