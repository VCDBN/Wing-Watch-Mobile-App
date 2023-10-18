package com.wingwatch.wingwatcher

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface eBirdApi {
    @Headers("X-eBirdApiToken: 1rkphdg8mrs6")
    @GET("recent")
    fun getData(@Query("maxResults") maxResults : Number,
                @Query("lat") lat : Double,
                @Query("lng") lng : Double): Observable<List<Species>>
}