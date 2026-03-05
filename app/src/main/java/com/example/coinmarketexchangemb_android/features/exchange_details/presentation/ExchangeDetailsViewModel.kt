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
import com.example.coinmarketexchangemb_android.features.exchanges_list.presentation.ExchangesListState
import com.example.coinmarketexchangemb_android.utils.Text
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ExchangeDetailsViewModel(
    private val repository: ExchangeDetailsRepository = ExchangeDetailsRepository()
): ViewModel() {
    var uiState by mutableStateOf<ExchangeDetailsState>(ExchangeDetailsState.Loading)
        private set

    fun fetchDetails(id: String) {
        uiState = ExchangeDetailsState.Loading

        viewModelScope.launch {
            runCatching {
                repository.fetchExchangeDetails(id)
            }.onSuccess { response ->
                val details = response.body()?.data?.get(id)
                if (response.isSuccessful) {
                    if (details != null) {
                        uiState = ExchangeDetailsState.Success(details)
                        fetchAssets(id, details)
                    } else {
                        uiState = ExchangeDetailsState.Error(Text.Resource(R.string.error_no_data))
                    }
                } else {
                    handleFailure(Text.Resource(R.string.error_api_prefix, listOf(response.code())))
                }
            }.onFailure { e ->
                if (e is CancellationException) throw e
                val errorMessage = e.localizedMessage
                val errorText = if (!errorMessage.isNullOrBlank()) {
                    Text.DynamicString(errorMessage)
                } else {
                    Text.Resource(R.string.error_network_failure)
                }
                handleFailure(errorText)
            }
        }
    }

    private suspend fun fetchAssets(id: String, details: ExchangeDetails) {
        runCatching {
            repository.fetchExchangeDetailsAssets(id)
        }.onSuccess { response ->
            val allDataMap = response.body()?.data
            val assetsList = allDataMap?.get(id) ?: emptyList()
            handleSuccess(details, assetsList)
        }.onFailure {
            handleSuccess(details, emptyList())
        }
    }

    private fun handleFailure(text: Text) {
        uiState = ExchangeDetailsState.Error(text)
    }

    private fun handleSuccess(details: ExchangeDetails, assets: List<Asset>) {
        uiState = ExchangeDetailsState.Success(
            exchangeDetails = details,
            assets = assets
        )
    }
}