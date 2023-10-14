package com.wingwatch.wingwatcher

import com.google.gson.annotations.SerializedName

data class Species(

    @SerializedName("speciesCode"     ) var speciesCode     : String?  = null,
    @SerializedName("comName"         ) var comName         : String?  = null,
    @SerializedName("sciName"         ) var sciName         : String?  = null,
    @SerializedName("locId"           ) var locId           : String?  = null,
    @SerializedName("locName"         ) var locName         : String?  = null,
    @SerializedName("obsDt"           ) var obsDt           : String?  = null,
    @SerializedName("howMany"         ) var howMany         : Int?     = null,
    @SerializedName("lat"             ) var lat             : Double?  = null,
    @SerializedName("lng"             ) var lng             : Double?  = null,
    @SerializedName("obsValid"        ) var obsValid        : Boolean? = null,
    @SerializedName("obsReviewed"     ) var obsReviewed     : Boolean? = null,
    @SerializedName("locationPrivate" ) var locationPrivate : Boolean? = null,
    @SerializedName("subId"           ) var subId           : String?  = null
)


