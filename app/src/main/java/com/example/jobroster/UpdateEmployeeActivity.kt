package com.example.jobroster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobroster.databinding.UpdateemployeelayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UpdateEmployeeActivity : ComponentActivity() {
    private lateinit var binding: UpdateemployeelayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UpdateemployeelayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = Firebase.auth
        val database = Firebase.database
        val usersRef = database.getReference("Users")
        val userId = auth.currentUser?.uid
        var recyclerView = binding.recyclerView
        var employeeList = ArrayList<User>()
        recyclerView.layoutManager = LinearLayoutManager(this)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val employeeList = ArrayList<User>()

                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        user?.let {
                            if(it.role != "Manager"){
                                employeeList.add(it)
                            }

                        }
                    }// Set the data in the adapter
                    val adapter = CustomAdapter(employeeList) { email ->
                        // Handle item click here, e.g., start IndividualEmployeeActivity with UID
                        val intent = Intent(this@UpdateEmployeeActivity, IndividualEmployeeActivity::class.java)
                        intent.putExtra("employeeUid", email)
                        startActivity(intent)
                    }

                    recyclerView.adapter = adapter

                } else {
                    // Handle the case where user data doesn't exist
                    Log.d("Current User", "Data does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("databaseerror", "Kuch to garbar hai")
            }
        })
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

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