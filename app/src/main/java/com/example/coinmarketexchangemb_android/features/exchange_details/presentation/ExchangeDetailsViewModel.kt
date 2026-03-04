package com.example.coinmarketexchangemb_android.features.exchange_details.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinmarketexchangemb_android.R
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetailsRepository
import com.example.coinmarketexchangemb_android.utils.Text
import kotlinx.coroutines.launch

class ExchangeDetailsViewModel : ViewModel() {
    private val repository =
        ExchangeDetailsRepository()

    var uiState by mutableStateOf<ExchangeDetailsState>(ExchangeDetailsState.Loading)
        private set

    fun fetchDetails(id: String) {
        viewModelScope.launch {
            uiState = ExchangeDetailsState.Loading
            runCatching {
                repository.getExchangeMetadata(id)
            }.onSuccess { response ->
                val detail = response.body()?.data?.get(id)
                if (detail != null) uiState = ExchangeDetailsState.Success(detail)
                else uiState = ExchangeDetailsState.Error(Text.Resource(R.string.error_no_data))
            }.onFailure {
                uiState = ExchangeDetailsState.Error(Text.Resource(R.string.error_network_failure))
            }
        }
    }
}