package com.wingwatch.wingwatcher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ObservationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observations)

        val btnAddObs = findViewById<Button>(R.id.btnAddObs)
        btnAddObs.setOnClickListener{
            val intent = Intent(this, AddObsActivity::class.java)
            startActivity(intent)
            finish()
        }

        HelperClass.updateObsList()

        val rvObs = findViewById<RecyclerView>(R.id.rvObservations)
        val adapterObs = ObsAdapter(GlobalVariables.observations){ observation ->
            val intent = Intent(this@ObservationsActivity, ViewObsActivity::class.java)
            intent.putExtra("obs", observation)
            startActivity(intent)
            finish()
        }
        rvObs.adapter = adapterObs
        rvObs.layoutManager = LinearLayoutManager(this@ObservationsActivity)
    }
    }
