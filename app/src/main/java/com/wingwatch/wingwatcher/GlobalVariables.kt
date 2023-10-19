package com.wingwatch.wingwatcher

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

object GlobalVariables {
    val coords: MutableList<HotSpot> = mutableListOf()
    var currentPosition = Postion(0.0,0.0)

    var directions = mutableListOf<Directions>()
    val observations : MutableList<Bird> = mutableListOf<Bird>()

    //Default is 25km
    //Changed in app through Settings Activity
    //'radius' is always in km
    // SettingsActivity does the conversion from miles if needed
    var radius: Double = 15.0


    fun fetchDataFromeBirdApi() {
        val ebirdCompositeDisposable = CompositeDisposable()
        val disposable = eBirdApiClient.buildService().getData(100, currentPosition.lat,
            currentPosition.lon,radius)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                fun getPoints(list : List<Species>)
                {
                    coords.clear()
                    list.forEach(){
                        coords.add(HotSpot(it.lng,it.lat))
                    }
                }
                getPoints(response)

            }, { error ->

                Log.e("Bad Req", error.message.toString())
            })
        ebirdCompositeDisposable.add(disposable)
    }
}