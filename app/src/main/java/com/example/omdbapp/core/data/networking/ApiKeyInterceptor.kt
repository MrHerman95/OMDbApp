package com.example.omdbapp.core.data.networking

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(
    private val apiKey: String,
    private val queryParameterName: String = "apikey"
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(queryParameterName, apiKey)
            .build()
        return chain.proceed(request.newBuilder().url(url).build())
    }
}
