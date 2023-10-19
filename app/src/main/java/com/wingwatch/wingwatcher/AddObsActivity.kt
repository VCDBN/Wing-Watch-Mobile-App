package com.wingwatch.wingwatcher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.wingwatch.wingwatcher.GlobalVariables.currentPosition
import com.wingwatch.wingwatcher.GlobalVariables.observations

class AddObsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addobs)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val etCommonName = findViewById<EditText>(R.id.etCommonName2)
        val npHowMany = findViewById<NumberPicker>(R.id.npHowMany)
        npHowMany.minValue = 1
        npHowMany.maxValue = 100

        btnAdd.setOnClickListener(){
            var commonName = etCommonName.text.toString()
            var howMany = npHowMany.value
            observations.add(Bird(commonName,howMany,currentPosition.lon, currentPosition.lat))

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}
