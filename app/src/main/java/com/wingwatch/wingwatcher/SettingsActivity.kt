package com.wingwatch.wingwatcher

import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wingwatch.wingwatcher.GlobalVariables.radius

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val npRadius = findViewById<NumberPicker>(R.id.npRadius)
        val switch = findViewById<Switch>(R.id.switchUnitType)
        val btnApply = findViewById<Button>(R.id.btnApplySettings)
        val tvRadius = findViewById<TextView>(R.id.tvRadius)

        val miles = "Search Radius (mi)"
        val kilometers = "Search Radius (km)"

        if (switch.isChecked) {
            //kilometer range selection
            npRadius.minValue = 1
            npRadius.maxValue = 50
            tvRadius.text = kilometers
        } else {
            //mile range selection
            npRadius.minValue = 1 //1mi = 1.609km
            npRadius.maxValue = 31 //31mi = 49.879km
            tvRadius.text = miles
        }


        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                npRadius.minValue = 1
                npRadius.maxValue = 50
                tvRadius.text = kilometers
            } else {
                npRadius.minValue = 1
                npRadius.maxValue = 31
                tvRadius.text = miles
            }
        }

        btnApply.setOnClickListener() {
            //unit selection checked before setting radius
            radius =
                if (switch.isChecked) {
                    npRadius.value.toDouble()
                } else {
                    //converting mile selection to kilometer
                    npRadius.value * 1.609
                }
        }
    }
}
