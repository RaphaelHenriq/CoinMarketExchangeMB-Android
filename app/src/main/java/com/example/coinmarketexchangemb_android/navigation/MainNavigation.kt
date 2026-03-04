package com.example.coinmarketexchangemb_android.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.coinmarketexchangemb_android.features.exchange_details.presentation.ExchangeDetailsScreen
import com.example.coinmarketexchangemb_android.features.exchanges_list.presentation.ExchangesListScreen

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = ExchangesListRoute
    ) {
        composable<ExchangesListRoute> {
            ExchangesListScreen(
                navController = navController,
                viewModel = viewModel()
            )
        }

        composable<ExchangeDetailsRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<ExchangeDetailsRoute>()
            ExchangeDetailsScreen(
                exchangeId = args.id.toString(),
                viewModel = viewModel(),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}