package com.wingwatch.wingwatcher

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

}