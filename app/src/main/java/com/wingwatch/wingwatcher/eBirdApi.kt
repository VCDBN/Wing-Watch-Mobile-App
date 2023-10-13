package com.wingwatch.wingwatcher

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface eBirdApi {
    @Headers("X-eBirdApiToken: 1rkphdg8mrs6")
    @GET("notable")
    fun getData(@Query("maxResults") maxResults : Number)
    : Observable<List<Species>>
}