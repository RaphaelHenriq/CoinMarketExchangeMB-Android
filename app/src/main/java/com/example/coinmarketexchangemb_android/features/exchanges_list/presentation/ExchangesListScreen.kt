package com.example.coinmarketexchangemb_android.features.exchanges_list.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.coinmarketexchangemb_android.R
import com.example.coinmarketexchangemb_android.features.exchanges_list.data.Exchange
import com.example.coinmarketexchangemb_android.navigation.ExchangeDetailsRoute
import com.example.coinmarketexchangemb_android.utils.toCurrency
import com.example.coinmarketexchangemb_android.utils.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangesListScreen(
    navController: NavHostController,
    viewModel: ExchangesListViewModel
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringResource(id = R.string.exchanges_list_title),
                    fontWeight = FontWeight.Bold
                ) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is ExchangesListState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ExchangesListState.Success -> {
                    ExchangesListContent(exchanges = state.list, navController)
                }
                is ExchangesListState.Error -> {
                    ErrorAlertDialog(
                        errorMessage = state.message.asString(),
                        onRetry = { viewModel.fetchExchangesList() }
                    )
                }
            }
        }
    }
}

@Composable
fun ExchangesListContent(
    exchanges: List<Exchange>,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(exchanges) { exchange ->
            ExchangesListItem(exchange = exchange, onClick = { id ->
                navController.navigate(ExchangeDetailsRoute(id = id))
            })

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun ExchangesListItem(
    exchange: Exchange,
    onClick: (Int) -> Unit
) {
    Surface(
        onClick = { onClick(exchange.id) },
        modifier = Modifier.fillMaxWidth(),
        color = colorScheme.surface
    ) {
        Row (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://s2.coinmarketcap.com/static/img/exchanges/64x64/${exchange.id}.png")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.ic_launcher_background),
                contentDescription = exchange.name,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = exchange.name,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(
                        id = R.string.exchanges_list_volume_label,
                        exchange.spotVolumeUsd.toCurrency()
                    ),
                    style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
                exchange.dateLaunched?.let { dateStr ->
                    Text(
                        text = stringResource(R.string.exchanges_list_launched_label, dateStr.toFormattedDate()),
                        style = typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

@Composable
fun ErrorAlertDialog(
    errorMessage: String,
    onRetry: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.error_dialog_title),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage, textAlign = TextAlign.Center)
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text(stringResource(id = R.string.error_button_retry))
                }
            }
        }
    )
}