package com.example.coinmarketexchangemb_android.features.exchange_details.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetails
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetailsAssetsResponse
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetailsRepository
import com.example.coinmarketexchangemb_android.features.exchange_details.data.ExchangeDetailsResponse
import com.example.coinmarketexchangemb_android.R
import com.example.coinmarketexchangemb_android.utils.Text
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val repository: ExchangeDetailsRepository = mockk()
    private lateinit var sut: ExchangeDetailsViewModel

    private val mockDetails = ExchangeDetails(
        id = 102,
        name = "Binance",
        logo = "url",
        description = "Desc",
        urls = null,
        makerFee = 0.0,
        takerFee = 0.0,
        dateLaunched = ""
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sut = ExchangeDetailsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchDetails should update state to Success when all API calls work`() = runTest {
        val id = "102"

        coEvery { repository.fetchExchangeDetails(id) } returns Response.success(
            ExchangeDetailsResponse(data = mapOf(id to mockDetails))
        )
        coEvery { repository.fetchExchangeDetailsAssets(id) } returns Response.success(
            ExchangeDetailsAssetsResponse(data = mapOf(id to emptyList()))
        )

        sut.fetchDetails(id)
        advanceUntilIdle()

        val state = sut.uiState
        assertTrue("Should be success", state is ExchangeDetailsState.Success)
        assertEquals("Binance", (state as ExchangeDetailsState.Success).exchangeDetails.name)
    }

    @Test
    fun `fetchDetails should update state to Error when API returns empty data`() = runTest {
        val id = "999"

        coEvery { repository.fetchExchangeDetails(id) } returns Response.success(
            ExchangeDetailsResponse(data = emptyMap())
        )

        sut.fetchDetails(id)
        advanceUntilIdle()

        assertTrue("Should be error due to data error", sut.uiState is ExchangeDetailsState.Error)
    }

    @Test
    fun `fetchDetails should update state to Error when network fails`() = runTest {
        val id = "102"

        coEvery { repository.fetchExchangeDetails(id) } throws Exception("Network Failure")

        sut.fetchDetails(id)
        advanceUntilIdle()

        assertTrue("Should be error due to network failure", sut.uiState is ExchangeDetailsState.Error)
    }

    @Test
    fun `fetchDetails should still show success when assets request fails`() = runTest {
        val id = "102"

        coEvery { repository.fetchExchangeDetails(id) } returns Response.success(
            ExchangeDetailsResponse(data = mapOf(id to mockDetails))
        )
        coEvery { repository.fetchExchangeDetailsAssets(id) } throws Exception("Assets Error")

        sut.fetchDetails(id)
        advanceUntilIdle()

        val state = sut.uiState
        assertTrue(state is ExchangeDetailsState.Success)
        assertEquals(0, (state as ExchangeDetailsState.Success).assets.size)
    }

    @Test
    fun `fetchDetails should update state to Error with API code when response is not successful`() = runTest {
        val id = "102"
        val errorCode = 500

        coEvery { repository.fetchExchangeDetails(id) } returns Response.error(
            errorCode,
            "Internal Server Error".toResponseBody(null)
        )

        sut.fetchDetails(id)
        advanceUntilIdle()

        val state = sut.uiState
        assertTrue("Should be error", state is ExchangeDetailsState.Error)

        val errorText = (state as ExchangeDetailsState.Error).message
        assertTrue(errorText is Text.Resource)
        assertEquals(R.string.error_api_prefix, (errorText as Text.Resource).resId)
        assertEquals(errorCode, errorText.args[0])
    }

    @Test
    fun `fetchDetails should update state to Error with dynamic message when exception has localizedMessage`() = runTest {
        val id = "102"
        val specificErrorMessage = "No internet connection"

        coEvery { repository.fetchExchangeDetails(id) } throws Exception(specificErrorMessage)

        sut.fetchDetails(id)
        advanceUntilIdle()

        val state = sut.uiState
        assertTrue(state is ExchangeDetailsState.Error)

        val errorText = (state as ExchangeDetailsState.Error).message
        assertTrue("Should be DynamicString", errorText is Text.DynamicString)
        assertEquals(specificErrorMessage, (errorText as Text.DynamicString).value)
    }
}