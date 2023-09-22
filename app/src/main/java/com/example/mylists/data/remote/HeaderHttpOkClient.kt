package com.example.mylists.data.remote

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HeaderHttpOkClient {

    const val BASE_URL_BAR = "https://api.mercadolibre.com"

    private lateinit var httpClient: OkHttpClient

    fun getHeaderOkHttpClient(): OkHttpClient {
        if (HeaderHttpOkClient::httpClient.isInitialized) {
            return httpClient
        } else {
            val httpClient = OkHttpClient().newBuilder()
            httpClient
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .callTimeout(1, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .addNetworkInterceptor { chain ->
                    val original = chain.request()
                    val originalHttpUrl = original.url()

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("charset", "utf-8")
                        .addQueryParameter("Content-Type", "text/xml")
                        .addQueryParameter("Accept", "*/*")
                        .build()

                    chain.proceed(
                        original.newBuilder().url(url).build()
                    )
                }

            return httpClient.build()
        }
    }
}