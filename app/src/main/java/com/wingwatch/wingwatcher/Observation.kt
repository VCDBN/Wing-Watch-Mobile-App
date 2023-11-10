package com.wingwatch.wingwatcher

data class Observation(
    val obsKey: String? = null,
    val userKey: String? = null,
    val species: String? =  null,
    val count: String? =  null,
    val date: String? = null,
    val lon: String? = null,
    val lat: String? = null,
    val imgUrl: String? = null
)