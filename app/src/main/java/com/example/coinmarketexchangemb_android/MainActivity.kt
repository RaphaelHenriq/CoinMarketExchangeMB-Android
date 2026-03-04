package com.example.coinmarketexchangemb_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.coinmarketexchangemb_android.features.exchanges_list.presentation.ExchangesListScreen
import com.example.coinmarketexchangemb_android.ui.theme.CoinMarketExchangeMBAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinMarketExchangeMBAndroidTheme {
                ExchangesListScreen()
            }
        }
    }
}