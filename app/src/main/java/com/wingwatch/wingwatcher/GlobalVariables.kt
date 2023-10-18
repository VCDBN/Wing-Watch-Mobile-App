package com.wingwatch.wingwatcher

object GlobalVariables {
    val coords: MutableList<HotSpot> = mutableListOf()
    var currentPosition = Postion(0.0,0.0)
    var directions = mutableListOf<Directions>()
}