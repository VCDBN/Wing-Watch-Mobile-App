package com.wingwatch.wingwatcher

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.wingwatch.wingwatcher.GlobalVariables.observations

class AddObsActivity : AppCompatActivity() {

    private lateinit var coordinatesProvider: CoordinatesProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addobs)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val etCommonName = findViewById<EditText>(R.id.etCommonName2)
        val npHowMany = findViewById<NumberPicker>(R.id.npHowMany)
        npHowMany.minValue = 1
        npHowMany.maxValue = 100


        coordinatesProvider = CoordinatesProvider(this)

        if (coordinatesProvider.checkLocationPermission()) {
            coordinatesProvider.requestLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        var latitude = 0.0
        var longitude = 0.0

        // Get current coordinates
        val coordinates = coordinatesProvider.getCoordinates()
        if (coordinates != null) {
            latitude = coordinates.latitude
            longitude = coordinates.longitude
            Log.i("LAT+++++++++++++++++++++++++++++++++", latitude.toString())
            Log.i("LNG+++++++++++++++++++++++++++++++++", longitude.toString())
        }

        btnAdd.setOnClickListener(){
            var commonName = etCommonName.text.toString()
            var howMany = npHowMany.value
            observations.add(Bird(commonName,howMany, longitude, latitude))

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
