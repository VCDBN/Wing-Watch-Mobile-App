package com.wingwatch.wingwatcher

object GlobalVariables {
    val coords: MutableList<HotSpot> = mutableListOf()
    var currentPosition = Postion(0.0,0.0)
    var observations : MutableList<Observation> = mutableListOf()
    var instructions : MutableList<String> = mutableListOf()
    var tempLat : Double? = 0.0
    var tempLon : Double? = 0.0
    //Default is 25km
    //Changed in app through Settings Activity
    //'radius' is always in km
    // SettingsActivity does the conversion from miles if needed
    var radius: Double = 15.0
}