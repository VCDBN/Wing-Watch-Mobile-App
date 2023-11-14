package com.wingwatch.wingwatcher

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.wingwatch.wingwatcher.GlobalVariables.observations
import com.wingwatch.wingwatcher.GlobalVariables.radius
import java.text.SimpleDateFormat
import java.util.Locale

class HelperClass {
    companion object {

        fun notAllSpaces(string: String): Boolean {
            val regex = Regex(".*\\S.*")
            return regex.matches(string)
        }

        fun getPrettyDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            return outputFormat.format(date!!)
        }

        fun updateObsList() {
            val auth = Firebase.auth
            val userKey = auth.currentUser?.email.toString()
            val database = FirebaseDatabase.getInstance()
            val obsRef = database.getReference("Observations")

            val query = obsRef.orderByChild("userKey").equalTo(userKey)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    observations.clear()
                    for (taskSnapshot in dataSnapshot.children) {
                        val observation = taskSnapshot.getValue(Observation::class.java)
                        observation?.let {
                            observations.add(it)
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

        fun writeUserToDatabase(user: User){
            val auth = Firebase.auth
            val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
            databaseReference.child(auth.currentUser!!.uid).setValue(user)
        }

        fun loadSettings() {
            val auth = Firebase.auth
            val email = auth.currentUser!!.email.toString()
            val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
            val query: Query = databaseReference.orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //get existing user and load settings
                        val user = dataSnapshot.children.first().getValue(User::class.java)
                        radius = user!!.radius
                        if(user.darkMode){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            Log.i("+_+_+_+_+_+LOADED_SETTINGS_+_+_+_+_+_+", "Settings: DARK, Rad: $radius")
                        }else{
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            Log.i("+_+_+_+_+_+LOADED_SETTINGS_+_+_+_+_+_+", "Settings: LIGHT, Rad: $radius")
                        }
                    } else {
                        // User does not exist, create a new user with default values
                        radius = 25.0
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        Log.i("+_+_+_+_+_+LOADED_SETTINGS_+_+_+_+_+_+", "Settings: DARK, Rad: $radius (DEFAULT)")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    println("Error: ${databaseError.message}")
                }
            })
        }
    }
}