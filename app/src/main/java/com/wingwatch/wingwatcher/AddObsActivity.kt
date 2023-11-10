package com.wingwatch.wingwatcher

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar

class AddObsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var uri: Uri
    private lateinit var storageRef: FirebaseStorage
    private var imageAttached = false

    private lateinit var coordinatesProvider: CoordinatesProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addobs)

        storageRef = FirebaseStorage.getInstance()
        auth = Firebase.auth

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val txtSpecies = findViewById<EditText>(R.id.txtSpecies)
        val npHowMany = findViewById<NumberPicker>(R.id.npHowMany)
        val imageView = findViewById<ImageView>(R.id.imgObsImage)

        //----------------------------------------------CODE ATTRIBUTION----------------------------------------------
        //Title (YouTube): "Upload Image to Firebase in Android Studio | Upload Image to Firebase Storage Kotlin"
        //Author: "Waseem Shakoor"
        //URL: "https://www.youtube.com/watch?v=VueRFU7ETOc&ab_channel=WaseemShakoor"
        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            imageView.setImageURI(it)
            imageAttached = true
            uri = it!!
        }
        imageView.setOnClickListener {
            galleryImage.launch("image/*")
        }
        //------------------------------------------END OF CODE ATTRIBUTION-------------------------------------------
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

        btnAdd.setOnClickListener() {

            Toast.makeText(this, "Uploading Observation", Toast.LENGTH_SHORT).show()
            val species = txtSpecies.text.toString()
            val count = npHowMany.value

            val observation = Observation(
                null,
                null,
                species,
                count.toString(),
                getCurrentDateTime(),
                longitude,
                latitude,
                null
            )

            if (imageAttached) {
                upload(observation)
            } else {
                uploadNoImage(observation)
            }
        }
    }

    private fun upload(observation: Observation) {
        //----------------------------------------------CODE ATTRIBUTION----------------------------------------------
        //Title (YouTube): "Upload Image to Firebase in Android Studio | Upload Image to Firebase Storage Kotlin"
        //Author: "Waseem Shakoor"
        //URL: "https://www.youtube.com/watch?v=VueRFU7ETOc&ab_channel=WaseemShakoor"
        storageRef.getReference("obs_images").child(System.currentTimeMillis().toString())
            .putFile(uri)
            .addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                        //------------------------------------------END OF CODE ATTRIBUTION-------------------------------------------
                        val dbObsRef = FirebaseDatabase.getInstance().getReference("Observations")
                        val obsKey = dbObsRef.push().key
                        val obsData = mapOf(
                            "obsKey" to obsKey.toString(),
                            "userKey" to auth.currentUser?.email.toString(),
                            "species" to observation.species,
                            "count" to observation.count,
                            "date" to observation.date,
                            "imgUrl" to it.toString(),
                            "lon" to observation.lon,
                            "lat" to observation.lat
                        )
                        dbObsRef.child(obsKey!!).setValue(obsData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@AddObsActivity,
                                    "Uploaded",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@AddObsActivity,
                                    "Upload failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    }
            }
    }

    private fun uploadNoImage(observation: Observation) {
        val dbObsRef = FirebaseDatabase.getInstance().getReference("Observations")
        val obsKey = dbObsRef.push().key
        val obsData = mapOf(
            "obsKey" to obsKey.toString(),
            "userKey" to auth.currentUser?.email.toString(),
            "species" to observation.species,
            "count" to observation.count,
            "date" to observation.date,
            "imgUrl" to null,
            "lon" to observation.lon,
            "lat" to observation.lat,
        )
        dbObsRef.child(obsKey!!).setValue(obsData)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Uploaded",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Upload failed",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }
}
