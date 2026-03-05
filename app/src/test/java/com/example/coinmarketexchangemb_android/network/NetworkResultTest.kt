package com.example.coinmarketexchangemb_android.network

import junit.framework.TestCase.assertEquals
import org.junit.Test

class NetworkResultTest {

    @Test
    fun `success should be correct data`() {
        val data = "Test Data"
        val result = NetworkResult.Success(data)

        assertEquals(data, result.data)
    }

    @Test
    fun `error should be correct message and code`() {
        val message = "Unauthorized"
        val code = 401
        val result = NetworkResult.Error(message, code)

        assertEquals(message, result.message)
        assertEquals(code, result.code)
    }
}