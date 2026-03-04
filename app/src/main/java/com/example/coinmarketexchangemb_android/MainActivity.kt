package com.example.coinmarketexchangemb_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.coinmarketexchangemb_android.navigation.MainNavigation
import com.example.coinmarketexchangemb_android.ui.theme.CoinMarketExchangeMBAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinMarketExchangeMBAndroidTheme {
                val navController = rememberNavController()
                MainNavigation(navController = navController)
            }
        }
    }
}