package com.example.coinmarketexchangemb_android.features.exchange_details.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinmarketexchangemb_android.R
import com.example.coinmarketexchangemb_android.features.exchange_details.data.Asset
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetails
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetailsRepository
import com.example.coinmarketexchangemb_android.utils.Text
import kotlinx.coroutines.launch

class ExchangeDetailsViewModel: ViewModel() {
    private val repository =
        ExchangeDetailsRepository()

    var uiState by mutableStateOf<ExchangeDetailsState>(ExchangeDetailsState.Loading)
        private set

    fun fetchDetails(id: String) {
        uiState = ExchangeDetailsState.Loading

        viewModelScope.launch {
            runCatching {
                repository.getExchangeDetails(id)
            }.onSuccess { response ->
                val details = response.body()?.data?.get(id)
                if (details != null) {
                    uiState = ExchangeDetailsState.Success(details)
                    fetchAssets(id, details)
                } else {
                    uiState = ExchangeDetailsState.Error(Text.Resource(R.string.error_no_data))
                }
            }.onFailure {
                uiState = ExchangeDetailsState.Error(Text.Resource(R.string.error_network_failure))
            }
        }
    }

    private suspend fun fetchAssets(id: String, details: ExchangeDetails) {
        runCatching {
            repository.getExchangeDetailsAssets(id)
        }.onSuccess { response ->
            val allDataMap = response.body()?.data
            val assetsList = allDataMap?.get(id) ?: emptyList()
            updateSuccessState(details, assetsList)
        }.onFailure {
            updateSuccessState(details, emptyList())
        }
    }

    private fun updateSuccessState(details: ExchangeDetails, assets: List<Asset>) {
        uiState = ExchangeDetailsState.Success(
            exchangeDetails = details,
            assets = assets
        )
    }
}