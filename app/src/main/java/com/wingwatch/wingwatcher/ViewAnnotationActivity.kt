package com.wingwatch.wingwatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ViewAnnotationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_annotation)

        val lat = intent.getDoubleExtra("lat",0.0)
        val lon = intent.getDoubleExtra("lon",0.0)
        val comName = intent.getStringExtra("comName")
        val obsDt = intent.getStringExtra("obsDt")
        val howMany = intent.getIntExtra("howMany", 0)

        val tvLat = findViewById<TextView>(R.id.tvDetailsLat)
        val tvLon = findViewById<TextView>(R.id.tvDetailsLon)
        val tvComName = findViewById<TextView>(R.id.tvDetailsComName)
        val tvHowMany = findViewById<TextView>(R.id.tvDetailsHowMany)
        val tvObsDt = findViewById<TextView>(R.id.tvDetailsObsDt)

        tvLat.text = lat.toString()
        tvLon.text = lon.toString()
        tvComName.text = comName
        tvHowMany.text = howMany.toString()
        tvObsDt.text = obsDt


    }
}