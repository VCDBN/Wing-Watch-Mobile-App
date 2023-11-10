package com.wingwatch.wingwatcher

import java.io.Serializable

data class Observation(
    val obsKey: String? = null,
    val userKey: String? = null,
    val species: String? =  null,
    val count: String? =  null,
    val date: String? = null,
    val lon: Double? = null,
    val lat: Double? = null,
    val imgUrl: String? = null,
) : Serializable //serializable allows passing the whole object using putExtra for an intent.