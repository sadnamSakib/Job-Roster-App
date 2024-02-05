package com.example.jobroster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobroster.databinding.SignuplayoutBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date


class SignUpActivity : AppCompatActivity() {
    private lateinit var dateOfBirthEditText: TextView
    private lateinit var selectDate: Button
    private var selectedDate: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SignuplayoutBinding.inflate(layoutInflater)
        val auth = Firebase.auth
        val database = Firebase.database
        val usersRef = database.getReference("Users")
        setContentView(binding.root)
        selectDate = binding.selectDateButton
        dateOfBirthEditText = binding.signupdate
        selectDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                dateOfBirthEditText.text = date

            }

        }
        binding.signupbutton.setOnClickListener {
            val name = binding.signupname.text.toString()

            val dob = binding.signupdate.text.toString()
            var gender : String = ""
            if(binding.signupmaleradiobutton.isChecked){
                gender = "Male"
            }
            else{
                gender = "Female"
            }
            val email = binding.signupemail.text.toString()
            val password = binding.signuppassword.text.toString()
            Log.d("userdata",email)
            Log.d("userdata",password)
            Log.d("userdata",dob)
            Log.d("userdata",name)
            Log.d("userdata",gender)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("Reached","Inside task completion")
                        val userId = auth.currentUser?.uid

                        val manager = User(name, email,dob,"Manager",gender)

                        userId?.let {
                            usersRef.child(it).setValue(manager)
                        }
                        intent = Intent(this, LogInActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {

                        val errorMessage = task.exception?.message
                        Log.e("AuthenticationError", errorMessage ?: "Unknown error")
                        Toast.makeText(baseContext, "Authentication failed: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.goToSignInButton.setOnClickListener(){
            intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



}

