package com.example.coinmarketexchangemb_android.network

import com.example.coinmarketexchangemb_android.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test

class ApiKeyInterceptorTest {

    private val interceptor = ApiKeyInterceptor()

    @Test
    fun `intercept should add CMC_PRO_API_KEY header to the request`() {
        val chain = mockk<Interceptor.Chain>()
        val initialRequest = Request.Builder()
            .url("https://api.coinmarketcap.com/v1/test")
            .build()

        val requestSlot = slot<Request>()

        every { chain.request() } returns initialRequest
        every { chain.proceed(capture(requestSlot)) } returns mockk(relaxed = true)

        interceptor.intercept(chain)

        val interceptedRequest = requestSlot.captured
        assertEquals(BuildConfig.CMC_API_KEY, interceptedRequest.header("X-CMC_PRO_API_KEY"))
    }
}