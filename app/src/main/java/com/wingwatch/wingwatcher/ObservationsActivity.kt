package com.wingwatch.wingwatcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ObservationsActivity : AppCompatActivity() {

    private lateinit var coordinatesProvider: CoordinatesProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observations)
    }
}