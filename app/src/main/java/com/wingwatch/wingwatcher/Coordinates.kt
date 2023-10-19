package com.wingwatch.wingwatcher

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat

data class Coordinates(val latitude: Double, val longitude: Double)

class CoordinatesProvider(private val context: Context) {

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {}
        override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
        //override fun onProviderEnabled(provider: String?) {}
        //override fun onProviderDisabled(provider: String?) {}
    }

    fun getCoordinates(): Coordinates? {
        if (checkLocationPermission()) {
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                val latitude = lastKnownLocation.latitude
                val longitude = lastKnownLocation.longitude
                return Coordinates(latitude, longitude)
            }
        }
        return null
    }

    fun checkLocationPermission(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationUpdates() {
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        }
    }

    fun removeLocationUpdates() {
        locationManager.removeUpdates(locationListener)
    }
}