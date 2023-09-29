package com.example.mylists.data.remote

import com.example.mylists.data.remote.request.RequestLoadProductJSL
import com.example.mylists.data.remote.response.ResponseLoadProductJSL
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface BarCodeRetrofitApi {

/*    @GET("/sites/MLB/search")
    fun getProductBarCode(
        @Query("q") codigoDeBarras: String,
        @Query("access_token") accessToken: String
    ): Call<String>*/

    @POST("buscar")
    fun getProductBarCodeJSL(
        @Body args: List<RequestLoadProductJSL>
    ): Call<List<ResponseLoadProductJSL>>

    companion object {
        fun getService(): BarCodeRetrofitApi {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(HeaderHttpOkClient.BASE_URL_BAR_JSL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(HeaderHttpOkClient.getHeaderOkHttpClient())
                .build()
            return retrofit.create(BarCodeRetrofitApi::class.java)
        }

       /* fun getService(): BarCodeRetrofitApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(HeaderHttpOkClient.BASE_URL_BAR)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(HeaderHttpOkClient.getHeaderOkHttpClient())
                .build()
            return retrofit.create(BarCodeRetrofitApi::class.java)
        }*/
    }
}