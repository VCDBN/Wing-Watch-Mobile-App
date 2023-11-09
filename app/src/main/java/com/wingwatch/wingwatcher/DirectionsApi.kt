package com.wingwatch.wingwatcher

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//----------------------------------------------CODE ATTRIBUTION----------------------------------------------
//Title : Directions API Mapbox
//Author: "Mapbox"
//URL: "https://docs.mapbox.com/api/navigation/directions/#retrieve-directions"
interface DirectionsApi {

        @GET("mapbox/driving/{coordinates}")
    fun getData(
            @Path(value = "coordinates", encoded = true) coordinates: String,
            @Query("alternatives") alternatives: Boolean,
            @Query("geometries") geometries: String,
            @Query("overview") overview: String,
            @Query("steps") steps: Boolean,
            @Query("language") language: String,
            @Query("access_token") access_token : String): Call<DirectionsResponse>
}