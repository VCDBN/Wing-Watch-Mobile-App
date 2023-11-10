package com.wingwatch.wingwatcher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ObservationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observations)

        val btnAddObs = findViewById<Button>(R.id.btnAddObs)
        btnAddObs.setOnClickListener{
            val intent = Intent(this, AddObsActivity::class.java)
            startActivity(intent)
        }
    }
}