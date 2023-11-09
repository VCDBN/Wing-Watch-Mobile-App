package com.wingwatch.wingwatcher

import com.google.gson.annotations.SerializedName


data class Routes (

    @SerializedName("weight_name" ) var weightName : String?         = null,
    @SerializedName("weight"      ) var weight     : Double?         = null,
    @SerializedName("duration"    ) var duration   : Double?         = null,
    @SerializedName("distance"    ) var distance   : Double?         = null,
    @SerializedName("legs"        ) var legs       : ArrayList<Legs> = arrayListOf(),
    @SerializedName("geometry"    ) var geometry   : Geometry?

)

data class MapboxStreetsV8 (

    @SerializedName("class" ) var class1 : String? = null

)
data class Intersections (

    @SerializedName("classes"           ) var classes         : ArrayList<String>  = arrayListOf(),
    @SerializedName("entry"             ) var entry           : ArrayList<Boolean> = arrayListOf(),
    @SerializedName("bearings"          ) var bearings        : ArrayList<Int>     = arrayListOf(),
    @SerializedName("duration"          ) var duration        : Double?            = null,
    @SerializedName("mapbox_streets_v8" ) var mapboxStreetsV8 : MapboxStreetsV8?   = MapboxStreetsV8(),
    @SerializedName("is_urban"          ) var isUrban         : Boolean?           = null,
    @SerializedName("admin_index"       ) var adminIndex      : Int?               = null,
    @SerializedName("out"               ) var out             : Int?               = null,
    @SerializedName("weight"            ) var weight          : Double?            = null,
    @SerializedName("geometry_index"    ) var geometryIndex   : Int?               = null,
    @SerializedName("location"          ) var location        : ArrayList<Double>  = arrayListOf()
)

data class Maneuver (

    @SerializedName("type"           ) var type          : String?           = null,
    @SerializedName("instruction"    ) var instruction   : String?           = null,
    @SerializedName("bearing_after"  ) var bearingAfter  : Int?              = null,
    @SerializedName("bearing_before" ) var bearingBefore : Int?              = null,
    @SerializedName("location"       ) var location      : ArrayList<Double> = arrayListOf()

)
data class Steps (

    @SerializedName("intersections" ) var intersections : ArrayList<Intersections> = arrayListOf(),
    @SerializedName("maneuver"      ) var maneuver      : Maneuver?,
    @SerializedName("name"          ) var name          : String?                  = null,
    @SerializedName("duration"      ) var duration      : Double?                  = null,
    @SerializedName("distance"      ) var distance      : Double?                  = null,
    @SerializedName("driving_side"  ) var drivingSide   : String?                  = null,
    @SerializedName("weight"        ) var weight        : Double?                  = null,
    @SerializedName("mode"          ) var mode          : String?                  = null,
    @SerializedName("ref"           ) var ref           : String?                  = null,
    @SerializedName("geometry"      ) var geometry      : Geometry?

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
    @SerializedName("steps"         ) var steps        : ArrayList<Steps>  = arrayListOf(),
    @SerializedName("distance"      ) var distance     : Double?           = null,
    @SerializedName("summary"       ) var summary      : String?           = null

)

data class Admins (

    @SerializedName("iso_3166_1_alpha3" ) var iso31661Alpha3 : String? = null,
    @SerializedName("iso_3166_1"        ) var iso31661       : String? = null

)


data class DirectionsResponse (
    @SerializedName("routes") val routes: List<Routes>,
    @SerializedName("waypoints" ) var waypoints : ArrayList<Waypoints> = arrayListOf(),
    @SerializedName("code"      ) var code : String? = null,
    @SerializedName("uuid"      ) var uuid : String? = null
)

data class RouteGeometry (
    @SerializedName("geometry") val geometry: Geometry
)

data class Geometry (
    @SerializedName("coordinates") val coordinates: List<List<Double>>,
    @SerializedName("type") val type: String
)