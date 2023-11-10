package com.wingwatch.wingwatcher

data class User(
    val email: String = "",
    var unitType: Boolean = true,
    var radius: Double = 25.0,
    var darkMode: Boolean = false
) {
    // No-argument constructor
    constructor() : this("")
}