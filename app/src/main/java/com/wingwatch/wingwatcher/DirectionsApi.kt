package com.wingwatch.wingwatcher

import com.mapbox.api.directions.v5.models.DirectionsRoute
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query



interface DirectionsApi {

        @GET("mapbox/driving/{coordinates}")
    fun getData(
            @Path(value = "coordinates", encoded = true) coordinates: String,
            @Query("alternatives") alternatives: Boolean,
            @Query("geometries") geometries: String,
            @Query("overview") overview: String,
            @Query("access_token") access_token : String): Call<RouteCustomResponse>
}