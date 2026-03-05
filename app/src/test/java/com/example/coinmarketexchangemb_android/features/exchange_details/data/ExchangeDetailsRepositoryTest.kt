package com.example.coinmarketexchangemb_android.features.exchange_details.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import com.example.coinmarketexchangemb_android.network.CoinMarketCapService

class ExchangeDetailsRepositoryTest {

    private val apiService: CoinMarketCapService = mockk()
    private val sut = ExchangeDetailsRepository(apiService)

    @Test
    fun `getExchangeDetails should call api and return success`() = runTest {
        val id = "102"
        val mockResponse = ExchangeDetailsResponse(data = emptyMap())

        coEvery { apiService.getExchangeDetails(id) } returns Response.success(mockResponse)

        val result = sut.fetchExchangeDetails(id)

        assertTrue(result.isSuccessful)
        assertEquals(mockResponse, result.body())
    }

    @Test
    fun `getExchangeDetailsAssets should call api and return assets`() = runTest {
        val id = "102"
        val mockAssetsResponse = ExchangeDetailsAssetsResponse(data = emptyMap())

        coEvery { apiService.getExchangeDetailsAssets(id) } returns Response.success(mockAssetsResponse)

        val result = sut.fetchExchangeDetailsAssets(id)

        assertTrue(result.isSuccessful)
        assertEquals(mockAssetsResponse, result.body())
    }
}