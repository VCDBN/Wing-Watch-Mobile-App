package com.wingwatch.wingwatcher

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object eBirdApiClient {

    private const val BASE_URL = "https://api.ebird.org/v2/data/obs/ZA/recent/"


    private val client = OkHttpClient()
        .newBuilder()
        .build()

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(eBirdApi::class.java)

    fun buildService(): eBirdApi {
        return retrofit
    }

}