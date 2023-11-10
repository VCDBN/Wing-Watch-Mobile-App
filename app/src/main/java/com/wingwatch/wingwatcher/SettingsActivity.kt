package com.wingwatch.wingwatcher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.wingwatch.wingwatcher.HelperClass.Companion.loadSettings
import com.wingwatch.wingwatcher.HelperClass.Companion.writeUserToDatabase
import kotlin.math.roundToInt

class SettingsActivity : AppCompatActivity() {

    private lateinit var npRadius: NumberPicker
    private lateinit var swUnit: Switch
    private lateinit var swDark: Switch
    private lateinit var btnApply: Button
    private lateinit var tvRadius: TextView
    private val miles = "Search Radius (mi)"
    private val kilometers = "Search Radius (km)"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        npRadius = findViewById(R.id.npRadius)
        swUnit = findViewById(R.id.swUnit)
        swDark = findViewById(R.id.swDarkMode)
        btnApply = findViewById(R.id.btnApplySettings)
        tvRadius = findViewById(R.id.tvRadius)

        settingsSetup()

        swUnit.setOnCheckedChangeListener { _, isChecked ->
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
            updateSettings()
        }
    }

    private fun settingsSetup() {
        val auth = Firebase.auth
        val email = auth.currentUser!!.email.toString()
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val query: Query = databaseReference.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    //get existing user and initialise settings
                    val user = dataSnapshot.children.first().getValue(User::class.java)
                    initSettingComponents(user!!)
                } else {
                    // User does not exist, create a new user with default values
                    val newUser = User(email, true, 25.0, false)
                    writeUserToDatabase(newUser)
                    settingsSetup()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        })
    }

    fun initSettingComponents(user: User){
        swUnit.isChecked = user.unitType
        swDark.isChecked = user.darkMode
        val radius = user.radius

        if (swUnit.isChecked) {
            //kilometer range selection
            npRadius.minValue = 1
            npRadius.maxValue = 50
            npRadius.value = radius.roundToInt()
            tvRadius.text = kilometers
        } else {
            //mile range selection
            npRadius.minValue = 1 //1mi = 1.609km
            npRadius.maxValue = 31 //31mi = 49.879km
            npRadius.value = (radius/1.609).roundToInt()
            tvRadius.text = miles
        }
    }

    fun updateSettings(){

        var unit: Boolean = false
        val radius =
            if (swUnit.isChecked) {
                unit = true
            npRadius.value.toDouble()
            } else {
                unit = false
            //converting mile selection to kilometer
            npRadius.value * 1.609
        }

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val auth = Firebase.auth
        val email = auth.currentUser!!.email.toString()
        val query: Query = databaseReference.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let {
                        // Edit the user data
                        it.darkMode = swDark.isChecked
                        it.radius = radius
                        it.unitType = unit
                        GlobalVariables.radius = radius
                        userSnapshot.ref.setValue(it)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    loadSettings()
                                    val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    println("Error writing user data to the database: ${task.exception}")
                                }
                            }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error or log
                println("Error: ${databaseError.message}")
            }
        })


    }
}
