package com.example.jobroster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobroster.databinding.IndividualemployeelayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class IndividualEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: IndividualemployeelayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IndividualemployeelayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = Firebase.auth
        val database = Firebase.database
        val usersRef = database.getReference("Users")
        val email = intent.getStringExtra("employeeUid")
        val jobScheduleRef = database.getReference("JobSchedule")
        val scheduleRecyclerView  = binding.scheduleRecyclerView
        scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        val updateScheduleButton = binding.updateScheduleButton
        val totalHoursTextView = binding.totalHoursTextView

        usersRef.orderByChild("email").equalTo(email.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        Log.d("ekhane", email!!)
                        Log.d("Ekhane", user.toString())
                        user?.let {
                            Log.d("ekhane", user.toString())
                            val userId = userSnapshot.key
                            val totalHours : Long = user.totalHours ?: 0
                            val hours: Long = totalHours / 60
                            val minutes : Long = totalHours % 60
                            totalHoursTextView.text = "Total time:"+ hours+ " hours " +  minutes + " minutes"
                            jobScheduleRef.child(userId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(scheduleSnapshot: DataSnapshot) {
                                    if (scheduleSnapshot.exists()) {
                                        // Retrieve the job schedule data here
                                        val jobSchedule = scheduleSnapshot.getValue(JobSchedule::class.java)
                                        Log.d("Job Schedule Data", jobSchedule.toString())
                                        jobSchedule?.let {

                                            val days : ArrayList<DaySchedule> = ArrayList()
                                            days.add(jobSchedule.monday!!)
                                            days.add(jobSchedule.tuesday!!)
                                            days.add(jobSchedule.wednesday!!)
                                            days.add(jobSchedule.thursday!!)
                                            days.add(jobSchedule.friday!!)
                                            days.add(jobSchedule.saturday!!)
                                            days.add(jobSchedule.sunday!!)

                                            scheduleRecyclerView.adapter = ScheduleAdapter(supportFragmentManager , days)
                                        }
                                    } else {
                                        // Handle the case where job schedule data doesn't exist
                                        Log.d("Job Schedule", "Data does not exist")
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("Job Schedule Error", "Something went wrong when fetching job schedule data")
                                }
                            })

                        }
                        break
                    }
                } else {
                    // Handle the case where user data doesn't exist
                    Log.d("Current User", "Data does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("databaseerror", "Kuch to garbar hai")
            }
        })

        updateScheduleButton.setOnClickListener(){
            val updatedJobSchedule = JobSchedule(email = email)
            var totalHours = 0L

            for (i in 0 until scheduleRecyclerView.childCount) {
                val viewHolder = scheduleRecyclerView.findViewHolderForAdapterPosition(i) as ScheduleAdapter.ViewHolder

                // Get the input field values from the ViewHolder
                val dayOfWeek = viewHolder.dayOfWeek.text.toString()
                val startTime = viewHolder.startTimeTextView.text.toString()
                val endTime = viewHolder.endTimeTextView.text.toString()
                totalHours += calculateTimeDifference(startTime, endTime)

                // Update the corresponding DaySchedule in the updatedJobSchedule
                when (dayOfWeek) {
                    "Monday" -> updatedJobSchedule.monday = DaySchedule(dayOfWeek, startTime, endTime)
                    "Tuesday" -> updatedJobSchedule.tuesday = DaySchedule(dayOfWeek, startTime, endTime)
                    "Wednesday" -> updatedJobSchedule.wednesday = DaySchedule(dayOfWeek, startTime, endTime)
                    "Thursday" -> updatedJobSchedule.thursday = DaySchedule(dayOfWeek, startTime, endTime)
                    "Friday" -> updatedJobSchedule.friday = DaySchedule(dayOfWeek, startTime, endTime)
                    "Saturday" -> updatedJobSchedule.saturday = DaySchedule(dayOfWeek, startTime, endTime)
                    "Sunday" -> updatedJobSchedule.sunday = DaySchedule(dayOfWeek, startTime, endTime)
                    else -> Log.e("Day of Week Error", "Invalid day of week")
                }
            }
            totalHoursTextView.text = "Total time : " + totalHours/60 + " hours " + totalHours%60 + " minutes"

            usersRef.orderByChild("email").equalTo(email.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            Log.d("ekhane", email!!)
                            Log.d("Ekhane", user.toString())
                            user?.let {
                                val userId = userSnapshot.key
                                jobScheduleRef.child(userId!!).setValue(updatedJobSchedule)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@IndividualEmployeeActivity, "Job schedule data has been updated.", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Log.e("Update Error", "Error updating job schedule data: ${it.message}")
                                        Toast.makeText(this@IndividualEmployeeActivity, "Failed to update job schedule data", Toast.LENGTH_SHORT).show()
                                    }
                                usersRef.child(userId).child("totalHours").setValue(totalHours)

                            }
                            break
                        }
                    } else {
                        // Handle the case where user data doesn't exist
                        Log.d("Current User", "Data does not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("databaseerror", "Kuch to garbar hai")
                }
            })
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

    fun calculateTimeDifference(startTime: String, endTime: String): Long {
        val dateFormat =SimpleDateFormat("HH:mm ", Locale.getDefault())
        val start = dateFormat.parse(startTime)
        val end = dateFormat.parse(endTime)
        return (end.time - start.time) / (60 * 1000) // Convert milliseconds to minutes
    }



}