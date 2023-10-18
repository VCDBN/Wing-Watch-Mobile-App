package com.wingwatch.wingwatcher

import android.util.Log
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.wingwatch.wingwatcher.GlobalVariables.directions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers



class Routing {
    val origin = "31.0377502,-29.8102634"
    val destination = "31.0341148,-29.7968864"
    fun getDirections()
    {
        val directionsCompositeDisposable = CompositeDisposable()

        val disposable = DirectionsClient.buildService(origin,destination).getData("$origin;$destination","sk.eyJ1IjoicGFwaWxvMSIsImEiOiJjbG51ZWdubGkwZGw4MnducHB3cHBtN2JwIn0.l0Ouw48vwhFyUNDfMMLZiw")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                directions.add(response)
            }, { error ->

                Log.e("Bad Req", error.message.toString())
            })


        directionsCompositeDisposable.add(disposable)

    }
}