package com.example.coinmarketexchangemb_android.features.exchanges_list.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinmarketexchangemb_android.R
import com.example.coinmarketexchangemb_android.features.exchanges_list.data.Exchange
import com.example.coinmarketexchangemb_android.features.exchanges_list.data.ExchangesListRepository
import com.example.coinmarketexchangemb_android.utils.Text
import kotlinx.coroutines.launch

class ExchangesListViewModel: ViewModel() {

    private val repository = ExchangesListRepository()

    var uiState by mutableStateOf<ExchangesListState>(ExchangesListState.Loading)
        private set

    init { fetchExchangesList() }

    fun fetchExchangesList() {
        uiState = ExchangesListState.Loading

        viewModelScope.launch {
            try {
                val response = repository.fetchExchangesList()
                if (response.isSuccessful) {
                    val exchanges = response.body()?.data
                    when {
                        exchanges == null -> handleFailure(Text.Resource(R.string.error_process_data))
                        exchanges.isEmpty() -> handleFailure(Text.Resource(R.string.error_no_data))
                        else -> handleSuccess(exchanges)
                    }
                } else {
                    handleFailure(
                        Text.Resource(
                            resId = R.string.error_api_prefix,
                            args = listOf(response.code())
                        )
                    )
                }

            } catch (e: Exception) {
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

    private fun handleFailure(text: Text) {
        uiState = ExchangesListState.Error(text)
    }

    private fun handleSuccess(exchanges: List<Exchange>) {
        val sortedExchanges = exchanges.sortedByDescending { it.spotVolumeUsd }
        uiState = ExchangesListState.Success(sortedExchanges)
    }
}