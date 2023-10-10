package com.example.wingwatcher

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
   private lateinit var password: EditText
    private lateinit var confirm: EditText
    private lateinit var inputEmail: String
    private lateinit var inputPassword: String
    private lateinit var inputConfirm: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        email = findViewById(R.id.txtRegisterEmail)
        password = findViewById(R.id.txtRegisterPassword)
        confirm = findViewById(R.id.txtRegisterConfirmPassword)
        var emailChanged = false
        var passwordChanged = false
        var confirmChanged = false

        //Live input validation for email
        email.doOnTextChanged { text, _, _, _ ->
            emailChanged = true
            if (text!!.isNotEmpty()) {
                if (HelperClass.notAllSpaces(text.toString())) {
                    email.error = null
                } else {
                    email.error = "Must include characters*"
                }
            } else {
                email.error = "Required*"
            }
        }
        auth = Firebase.auth

        //Live input validation for password
        password.doOnTextChanged { text, _, _, _ ->
            passwordChanged = true
            val l = text!!.length
            if (text.isNotEmpty()) {
                if (HelperClass.notAllSpaces(text.toString())) {
                    if (l < 6) {
                        password.error = "Min 6 Characters"
                    } else {
                        password.error = null
                    }
                } else {
                    password.error = "Must include characters*"
                }
            } else {
                password.error = "Required*"
            }
        }

        //Live input validation for password confirmation
        confirm.doOnTextChanged { text, _, _, _ ->
            confirmChanged = true
           inputPassword = password.text.toString()
            if (text!!.isNotEmpty()) {
                if (HelperClass.notAllSpaces(text.toString())) {
                    if (text.toString() != inputPassword) {
                        confirm.error = "Passwords do not Match"
                    } else {
                        confirm.error = null
                    }
                } else {
                    confirm.error = "Must include characters*"
                }
            } else {
                confirm.error = "Required*"
            }
        }

       // auth = Firebase.auth

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener()
        {
            inputEmail = email.text.toString()
            inputPassword=password.text.toString()
            inputConfirm = confirm.text.toString()

            //validation checks if there are no errors and that the values have been changed by the user to trigger errors.
            if (emailChanged && passwordChanged && confirmChanged && email.error == null && password.error == null && confirm.error == null) {
                //Registration Process is started
                performRegistration(inputEmail,inputPassword)
            } else {
                // If input validation fails
                Toast.makeText(this, "Incorrect or Missing Fields", Toast.LENGTH_SHORT).show()
            }


        }
        val tvLogin = findViewById<TextView>(R.id.tvLogin)
        tvLogin.setOnClickListener()
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

    }
    fun performRegistration(inputEmail:String, inputPassword:String) { //Registration process creates a firebase auth user and adds a user reference to firebase real-time database
        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    //This activity is closed, when the user tries to return, they will be taken to the login activity
                    finish()

                } else {
                    // If registration process fails
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}