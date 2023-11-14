package com.wingwatch.wingwatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.wingwatch.wingwatcher.GlobalVariables.instructions

class ViewDirectionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_directions)

        val instructionsList = instructions
        val tvInstructions = findViewById<TextView>(R.id.tvInstructions)

        if (instructionsList != null) {
            val concatenatedString = instructionsList.joinToString("\n-")
            tvInstructions.text = "\n$concatenatedString"
        } else {
            tvInstructions.text = "No instructions to display"
        }
    }
}