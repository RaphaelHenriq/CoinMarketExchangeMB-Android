package com.example.coinmarketexchangemb_android.features.exchanges_list.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import com.example.coinmarketexchangemb_android.network.CoinMarketCapService

class ExchangesListRepositoryTest {

    private val apiService: CoinMarketCapService = mockk()
    private val sut = ExchangesListRepository(apiService)

    @Test
    fun `getExchangeDetails should call api and return success`() = runTest {
        val id = "102"
        val mockResponse = ExchangesListResponse(data = emptyList())

        coEvery { apiService.getExchangesList() } returns Response.success(mockResponse)

        val result = sut.fetchExchangesList()

        assertTrue(result.isSuccessful)
        assertEquals(mockResponse, result.body())
    }
}