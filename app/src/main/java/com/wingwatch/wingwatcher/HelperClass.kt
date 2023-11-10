package com.wingwatch.wingwatcher

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.wingwatch.wingwatcher.GlobalVariables.observations
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
    }
}