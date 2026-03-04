package com.example.coinmarketexchangemb_android.features.exchange_details.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.coinmarketexchangemb_android.R
import com.example.coinmarketexchangemb_android.features.exchange_details.data.Asset
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetails
import com.example.coinmarketexchangemb_android.features.exchanges_list.presentation.ErrorAlertDialog
import com.example.coinmarketexchangemb_android.utils.toCurrency
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDetailsScreen(
    exchangeId: String,
    viewModel: ExchangeDetailsViewModel,
    onBackClick: () -> Unit
) {
    val state = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = exchangeId) {
        viewModel.fetchDetails(exchangeId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state is ExchangeDetailsState.Success) stringResource(id = R.string.exchange_details, state.exchangeDetails.name) else stringResource(id = R.string.exchange_details),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.exchange_details_back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is ExchangeDetailsState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is ExchangeDetailsState.Success -> {
                    ExchangeDetailsContent(
                        state.exchangeDetails,
                        state.assets,
                        onLinkError= { message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                            }
                        }
                    )
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
fun ExchangeDetailsContent(
    details: ExchangeDetails,
    assets: List<Asset>,
    onLinkError: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { MainInfoCard(details) }

        item { SectionTitle(stringResource(id = R.string.exchange_details)) }

        item { TechnicalDetailsCard(details, onLinkError) }

        item { SectionTitle(stringResource(id = R.string.exchange_details)) }

        items(assets) { AssetItem(it) }

        if (assets.isEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.exchanges_details_no_coin),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        assets.forEachIndexed { index, asset ->
                            AssetRow(asset)
                            if (index < assets.size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainInfoCard(detail: ExchangeDetails) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = detail.logo,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = detail.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.exchange_details_id, detail.id),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = detail.description ?: stringResource(R.string.exchange_details_no_description),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                lineHeight = 20.sp,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TechnicalDetailsCard(
    detail: ExchangeDetails,
    onLinkError: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val websiteUrl = detail.urls?.website?.firstOrNull()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            DetailRow(
                label = stringResource(id = R.string.exchange_details_website),
                value = websiteUrl?.removePrefix(stringResource(id = R.string.exchange_details_prefix_http)) ?: stringResource(id = R.string.exchange_details_na),
                isLink = true,
                onClick = {
                    websiteUrl?.let { url ->
                        runCatching {
                            uriHandler.openUri(url)
                        }.onFailure {
                            onLinkError("Não foi possível abrir o link.")
                        }
                    }
                }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            DetailRow(label = stringResource(id = R.string.exchange_details_launched), value = detail.dateLaunched?.substringBefore("T") ?: stringResource(id = R.string.exchange_details_na))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            DetailRow(label = stringResource(id = R.string.exchange_details_maker_fee), value = "${detail.makerFee ?: stringResource(id = R.string.exchange_details_zero)}%")
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            DetailRow(label = stringResource(id = R.string.exchange_details_taker_fee), value = "${detail.takerFee ?: stringResource(id = R.string.exchange_details_zero)}%")
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    isLink: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isLink) MaterialTheme.colorScheme.primary else Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f).padding(start = 16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.Gray,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun AssetItem(asset: Asset) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = asset.currency.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = asset.currency.priceUsd.toCurrency(),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AssetRow(asset: Asset) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = asset.currency.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "$ ${asset.currency.priceUsd.toCurrency()}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End
        )
    }
}