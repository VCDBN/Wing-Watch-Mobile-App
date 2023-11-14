package com.wingwatch.wingwatcher

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ViewObsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewobs)
        val helper = HelperClass

        //object
        val obs = intent.getSerializableExtra("obs") as Observation

        //show directions
        val showDirection = intent.getBooleanExtra("showDirection",false)

        if(showDirection)
        {
            val btnShowDirections = findViewById<Button>(R.id.btnShowDirections)
            btnShowDirections.visibility = View.VISIBLE
            btnShowDirections.setOnClickListener(){
                val intent = Intent(this, ViewDirectionsActivity::class.java)
                startActivity(intent)
            }
        }

        //components
        val imgObs = findViewById<ImageView>(R.id.imgObs)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvSpecies = findViewById<TextView>(R.id.tvBirdSpecies)
        val tvCount = findViewById<TextView>(R.id.tvCount)
        val tvLon = findViewById<TextView>(R.id.tvLon)
        val tvLat = findViewById<TextView>(R.id.tvLat)
        val tvImageHint = findViewById<TextView>(R.id.tvImageHint)


        //set values

        //image loading with picasso
        if(obs.imgUrl != "null") {
            val imageUrl = obs.imgUrl
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgObs)
        }

        //details
        tvDate.text = helper.getPrettyDate(obs.date!!)
        tvSpecies.text = obs.species
        tvCount.text = obs.count
        tvLon.text = obs.lon.toString()
        tvLat.text = obs.lat.toString()

        val initialHeight = resources.getDimensionPixelSize(R.dimen.initial_height)
        val expandedHeight = resources.getDimensionPixelSize(R.dimen.expanded_height)
        imgObs.setOnClickListener {
            val layoutParams = imgObs.layoutParams
            tvImageHint.text = ""
            if (layoutParams.height == initialHeight) {
                // Expand the height to 510dp
                layoutParams.height = expandedHeight
            } else {
                // Restore the initial height
                layoutParams.height = initialHeight
            }
            imgObs.layoutParams = layoutParams
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this, ObservationsActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}