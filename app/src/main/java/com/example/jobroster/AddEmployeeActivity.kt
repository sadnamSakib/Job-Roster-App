package com.example.jobroster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.jobroster.databinding.AddemployeelayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddEmployeeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AddemployeelayoutBinding.inflate(layoutInflater)
        val auth = Firebase.auth
        val database = Firebase.database
        val usersRef = database.getReference("Users")
        val jobScheduleRef = database.getReference("JobSchedule")
        setContentView(binding.root)
        val roleSpinner = binding.roleSpinner
        val roleOptions = resources.getStringArray(R.array.roles)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roleOptions)
        roleSpinner.adapter = adapter
        binding.signupbutton.setOnClickListener {
            val name = binding.signupname.text.toString()
            val dob = binding.signupdate.text.toString()

            val role = roleSpinner.selectedItem.toString()
            var gender: String = ""
            if (binding.signupmaleradiobutton.isChecked) {
                gender = "Male"
            } else {
                gender = "Female"
            }
            val email = binding.signupemail.text.toString()
            val password = binding.signuppassword.text.toString()


            Log.d("Reached", "Inside task completion")
            val userId = usersRef.push().key

            val employee = User(name, email, dob, role, gender)

            userId?.let {
                usersRef.child(it).setValue(employee)
                var monday: DaySchedule = DaySchedule("Monday", "00:00", "00:00")
                var tuesday: DaySchedule = DaySchedule("Tuesday", "00:00", "00:00")
                var wednesday: DaySchedule = DaySchedule("Wednesday", "00:00", "00:00")
                var thursday: DaySchedule = DaySchedule("Thursday", "00:00", "00:00")
                var friday: DaySchedule = DaySchedule("Friday", "00:00", "00:00")
                var saturday: DaySchedule = DaySchedule("Saturday", "00:00", "00:00")
                var sunday: DaySchedule = DaySchedule("Sunday", "00:00", "00:00")
                jobScheduleRef.child(it).setValue(
                    JobSchedule(
                        email,
                        monday,
                        tuesday,
                        wednesday,
                        thursday,
                        friday,
                        saturday,
                        sunday
                    )
                )
            }
            intent = Intent(this, AddEmployeeActivity::class.java)
            Toast.makeText(baseContext, "Employee Added Successfully", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()


        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set up the item click listener for the BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_profile -> {
                    // Handle the profile item click (optional)
                    // You can stay on the current activity or navigate to a different profile-related activity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true // Return true to indicate that the item click is handled
                }

                R.id.navigation_AddEmployee -> {
                    // Launch the AddEmployeeActivity
                    val intent = Intent(this, AddEmployeeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.Navigation_UpdateEmployee -> {
                    // Launch the UpdateEmployeeActivity
                    val intent = Intent(this, UpdateEmployeeActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

    }
}