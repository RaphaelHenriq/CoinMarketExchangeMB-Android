package com.example.coinmarketexchangemb_android.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toFormattedDate(): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        this
    }
}