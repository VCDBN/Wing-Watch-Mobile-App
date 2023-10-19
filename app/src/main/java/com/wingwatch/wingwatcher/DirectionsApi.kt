package com.wingwatch.wingwatcher

import com.mapbox.api.directions.v5.models.DirectionsRoute
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query



interface DirectionsApi {

        @GET("mapbox/driving/{coordinates}")
    fun getData(
            @Path("coordinates") coordinates: String,
        @Query("access_token") access_token : String): Observable<Directions>
}