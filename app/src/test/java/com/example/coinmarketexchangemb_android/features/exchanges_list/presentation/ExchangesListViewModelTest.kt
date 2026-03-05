package com.example.coinmarketexchangemb_android.features.exchanges_list.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coinmarketexchangemb_android.R
import com.example.coinmarketexchangemb_android.features.exchanges_list.data.Exchange
import com.example.coinmarketexchangemb_android.features.exchanges_list.data.ExchangesListRepository
import com.example.coinmarketexchangemb_android.features.exchanges_list.data.ExchangesListResponse
import com.example.coinmarketexchangemb_android.features.exchanges_list.data.Quote
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
import kotlin.collections.listOf

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangesListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val repository: ExchangesListRepository = mockk()
    private lateinit var sut: ExchangesListViewModel

    private val mockExchange = Exchange(
        id = 102,
        name = "Binance",
        logo = "url",
        quote = Quote(usd = null),
        dateLaunched = "dateLaunched"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sut = ExchangesListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchExchangesList should update state to Success when all API calls work`() = runTest {
        coEvery { repository.fetchExchangesList() } returns Response.success(
            ExchangesListResponse(data = listOf(mockExchange))
        )

        sut.fetchExchangesList()
        advanceUntilIdle()

        val state = sut.uiState
        assertTrue("Should be success", state is ExchangesListState.Success)
        assertEquals("Binance", (state as ExchangesListState.Success).list.get(0).name)
    }

    @Test
    fun `fetchExchangesList should update state to Error when API returns empty data`() = runTest {
        coEvery { repository.fetchExchangesList() } returns Response.success(
            ExchangesListResponse(data = listOf())
        )

        sut.fetchExchangesList()
        advanceUntilIdle()

        assertTrue("Should be error due to data error", sut.uiState is ExchangesListState.Error)
    }

    @Test
    fun `fetchExchangesList should update state to Error when network fails`() = runTest {
        coEvery { repository.fetchExchangesList() } throws Exception("Network Failure")

        sut.fetchExchangesList()
        advanceUntilIdle()

        assertTrue("Should be error due to network failure", sut.uiState is ExchangesListState.Error)
    }

    @Test
    fun `fetchExchangesList should update state to Error with API code when response is not successful`() = runTest {
        val errorCode = 500

        coEvery { repository.fetchExchangesList() } returns Response.error(
            errorCode,
            "Internal Server Error".toResponseBody(null)
        )

        sut.fetchExchangesList()
        advanceUntilIdle()

        val state = sut.uiState
        assertTrue("Should be error", state is ExchangesListState.Error)

        val errorText = (state as ExchangesListState.Error).message
        assertTrue(errorText is Text.Resource)
        assertEquals(R.string.error_api_prefix, (errorText as Text.Resource).resId)
        assertEquals(errorCode, errorText.args[0])
    }

    @Test
    fun `fetchExchangesList should update state to Error with dynamic message when exception has localizedMessage`() = runTest {
        val specificErrorMessage = "No internet connection"

        coEvery { repository.fetchExchangesList() } throws Exception(specificErrorMessage)

        sut.fetchExchangesList()
        advanceUntilIdle()

        val state = sut.uiState
        assertTrue(state is ExchangesListState.Error)

        val errorText = (state as ExchangesListState.Error).message
        assertTrue("Should be DynamicString", errorText is Text.DynamicString)
        assertEquals(specificErrorMessage, (errorText as Text.DynamicString).value)
    }
}