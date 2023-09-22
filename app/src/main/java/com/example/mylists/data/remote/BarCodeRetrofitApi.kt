package com.example.mylists.data.remote

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BarCodeRetrofitApi {

    @GET("/sites/MLB/search")
    fun getProductBarCode(
        @Query("q") codigoDeBarras: String,
        @Query("access_token") accessToken: String
    ): Call<String>

    companion object {
        fun getService(): BarCodeRetrofitApi {

            val retrofit = Retrofit.Builder()
                .baseUrl(HeaderHttpOkClient.BASE_URL_BAR)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(HeaderHttpOkClient.getHeaderOkHttpClient())
                .build()
            return retrofit.create(BarCodeRetrofitApi::class.java)
        }
    }
}