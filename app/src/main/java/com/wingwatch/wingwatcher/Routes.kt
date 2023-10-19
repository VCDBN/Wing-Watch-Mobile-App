package com.wingwatch.wingwatcher

import com.google.gson.annotations.SerializedName


data class Routes (

    @SerializedName("weight_name" ) var weightName : String?         = null,
    @SerializedName("weight"      ) var weight     : Double?         = null,
    @SerializedName("duration"    ) var duration   : Double?         = null,
    @SerializedName("distance"    ) var distance   : Double?         = null,
    @SerializedName("legs"        ) var legs       : ArrayList<Legs> = arrayListOf(),
    @SerializedName("geometry"    ) var geometry   : String?         = null

)


data class Waypoints (

    @SerializedName("distance" ) var distance : Double?           = null,
    @SerializedName("name"     ) var name     : String?           = null,
    @SerializedName("location" ) var location : ArrayList<Double> = arrayListOf()

)

data class Legs (

    @SerializedName("via_waypoints" ) var viaWaypoints : ArrayList<String> = arrayListOf(),
    @SerializedName("admins"        ) var admins       : ArrayList<Admins> = arrayListOf(),
    @SerializedName("weight"        ) var weight       : Double?           = null,
    @SerializedName("duration"      ) var duration     : Double?           = null,
    @SerializedName("steps"         ) var steps        : ArrayList<String> = arrayListOf(),
    @SerializedName("distance"      ) var distance     : Double?           = null,
    @SerializedName("summary"       ) var summary      : String?           = null

)

data class Admins (

    @SerializedName("iso_3166_1_alpha3" ) var iso31661Alpha3 : String? = null,
    @SerializedName("iso_3166_1"        ) var iso31661       : String? = null

)

data class Directions (

    @SerializedName("routes"    ) var routes    : ArrayList<Routes>    = arrayListOf(),
    @SerializedName("waypoints" ) var waypoints : ArrayList<Waypoints> = arrayListOf(),
    @SerializedName("code"      ) var code      : String?              = null,
    @SerializedName("uuid"      ) var uuid      : String?              = null

)

data class RouteCustomResponse (
    @SerializedName("routes") val routes: List<CustomRoute>,
    @SerializedName("code"      ) var code : String? = null,
    @SerializedName("uuid"      ) var uuid : String? = null
)

data class CustomRoute (
    @SerializedName("geometry") val geometry: CustomGeometry
)

data class CustomGeometry (
    @SerializedName("coordinates") val coordinates: List<List<Double>>,
    @SerializedName("type") val type: String
)