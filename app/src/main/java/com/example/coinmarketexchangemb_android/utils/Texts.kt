package com.example.coinmarketexchangemb_android.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class Text {
    data class DynamicString(val value: String) : Text()
    data class Resource(val resId: Int, val args: List<Any> = emptyList()) : Text()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is Resource -> stringResource(resId, *args.toTypedArray())
        }
    }
}