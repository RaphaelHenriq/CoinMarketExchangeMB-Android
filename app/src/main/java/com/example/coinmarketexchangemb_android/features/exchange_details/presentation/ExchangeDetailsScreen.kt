package com.example.coinmarketexchangemb_android.features.exchange_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeAsset
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetails
import com.example.coinmarketexchangemb_android.features.exchanges_list.presentation.ErrorAlertDialog
import com.example.coinmarketexchangemb_android.features.exchanges_list.presentation.ExchangesListState
import com.example.coinmarketexchangemb_android.utils.toCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailsScreen(
    exchangeId: String,
    viewModel: ExchangeDetailsViewModel,
    onBackClick: () -> Unit
) {
    LaunchedEffect(key1 = exchangeId) {
        viewModel.fetchDetails(exchangeId)
    }

    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (state is ExchangeDetailsState.Success) state.exchangeDetails.name else "")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                is ExchangeDetailsState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is ExchangeDetailsState.Success -> {
                    ExchangeDetailsContent(state.exchangeDetails)
                }
                is ExchangeDetailsState.Error -> {
                    ErrorAlertDialog(
                        errorMessage = state.message.asString(),
                        onRetry = { viewModel.fetchDetails(exchangeId) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExchangeDetailsContent(detail: ExchangeDetails) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F7)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
//            MainInfoCard(detail)
        }

        item {
            Text(
                text = "Detalhes",
                style = typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

//        item {
//            TechnicalDetailsCard(detail)
//        }

        item {
            Text(
                text = "Ativos (Moedas)",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )
        }

//        items(detail.assets ?: emptyList()) { asset ->
//            AssetItem(asset)
//        }
    }
}

@Composable
fun AssetItem(asset: ExchangeAsset) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = asset.name,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = asset.priceUsd.toCurrency(),
                style = typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}