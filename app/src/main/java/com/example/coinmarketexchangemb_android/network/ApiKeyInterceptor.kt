package com.example.coinmarketexchangemb_android.network

import com.example.coinmarketexchangemb_android.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-CMC_PRO_API_KEY", BuildConfig.CMC_API_KEY)
            .build()
        return chain.proceed(request)
    }
}