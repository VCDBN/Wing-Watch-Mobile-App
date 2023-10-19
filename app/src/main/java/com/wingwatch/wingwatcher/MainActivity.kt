package com.wingwatch.wingwatcher

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.wingwatch.wingwatcher.GlobalVariables.coords
import com.wingwatch.wingwatcher.GlobalVariables.currentPosition
import com.wingwatch.wingwatcher.GlobalVariables.radius
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var coordinatesProvider: CoordinatesProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            currentPosition.lat = latitude
            currentPosition.lon = longitude

            Log.i("LAT+++++++++++++++++++++++++++++++++", currentPosition.lat.toString())
            Log.i("LNG+++++++++++++++++++++++++++++++++", currentPosition.lon.toString())
        }

        val btnSettings = findViewById<Button>(R.id.btnSettings)
        btnSettings.setOnClickListener(){
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val ebirdCompositeDisposable = CompositeDisposable()

        fun fetchDataFromeBirdApi() {

            val disposable = eBirdApiClient.buildService().getData(100, currentPosition.lat,
                currentPosition.lon,radius)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->

                    getPoints(response)

                }, { error ->

                    Log.e("Bad Req", error.message.toString())
                })
            ebirdCompositeDisposable.add(disposable)
        }

        fetchDataFromeBirdApi()


        val btnMap = findViewById<Button>(R.id.btnMap)

        btnMap.setOnClickListener(){
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }


        val btnExit = findViewById<Button>(R.id.btnExit)

        btnExit.setOnClickListener(){
            finishAffinity()
        }

        val btnAddObs = findViewById<Button>(R.id.btnAddObs)
        btnAddObs.setOnClickListener(){
            val intent = Intent(this, AddObsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getPoints(list : List<Species>)
    {
        coords.clear()
        list.forEach(){
            coords.add(HotSpot(it.lng,it.lat))
        }
    }

}