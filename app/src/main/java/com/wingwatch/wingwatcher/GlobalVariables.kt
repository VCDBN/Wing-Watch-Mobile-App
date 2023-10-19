package com.wingwatch.wingwatcher

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

object GlobalVariables {
    val coords: MutableList<HotSpot> = mutableListOf()
    var currentPosition = Postion(0.0,0.0)
    val observations : MutableList<Bird> = mutableListOf()

    //Default is 25km
    //Changed in app through Settings Activity
    //'radius' is always in km
    // SettingsActivity does the conversion from miles if needed
    var radius: Double = 15.0



}