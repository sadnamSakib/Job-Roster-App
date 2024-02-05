package com.example.jobroster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.jobroster.databinding.ProfilelayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : ComponentActivity() {

    private lateinit var binding: ProfilelayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfilelayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = Firebase.auth
        val database = Firebase.database
        val usersRef = database.getReference("Users")
        val userId = auth.currentUser?.uid
        val profilePicture = binding.profilePictureView
        Log.d("Current User", userId!!)

        usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.d("snapshot","snapshot exist kore")
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        binding.nameTextView.setText(it.name)
                        binding.dobtextView.setText(it.dob)
                        binding.emailTextView.setText(it.email)
                        binding.genderTextView.setText(it.gender)
                        binding.roleTextView.setText(it.role)
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

        val updateButton = binding.updateButton
        updateButton.setOnClickListener(){
            val updatedUser = User(
                binding.nameTextView.text.toString(),
                binding.emailTextView.text.toString(),
                binding.dobtextView.text.toString(),
                binding.roleTextView.text.toString(),
                binding.genderTextView.text.toString()

            )

                        usersRef.child(userId).setValue(updatedUser)
                            .addOnSuccessListener {
                                // Data updated successfully
                                Toast.makeText(applicationContext, "User data updated", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                // Handle the case where data update fails
                                Log.e("Update User Data", "Error updating user data: $it")
                                Toast.makeText(applicationContext, "Failed to update user data", Toast.LENGTH_SHORT).show()
                            }

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
