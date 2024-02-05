package com.example.jobroster

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.format.DateTimeFormatter

class CustomAdapter(private val mList: List<User>,private val itemClickListener: (String) -> Unit) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.employeecard, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.headingTextView.text = itemsViewModel?.name
        holder.subHeadingTextView.text = itemsViewModel?.role
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")
        val formattedDate = itemsViewModel?.dob?.format(dateTimeFormatter)
        holder.dateTextView.text = formattedDate

        holder.itemView.setOnClickListener {
            val uniqueIdentifier = itemsViewModel?.email // Replace with the appropriate property
            uniqueIdentifier?.let {
                itemClickListener(it)
            }
        }
        holder.deleteButton.setOnClickListener {
            val email = itemsViewModel?.email

            val alertDialog = AlertDialog.Builder(holder.itemView.context)
            alertDialog.setTitle("Delete User")
            alertDialog.setMessage("Are you sure you want to delete this user?")

            alertDialog.setPositiveButton("Delete") { _, _ ->
                // Delete the user from both "Users" and "JobSchedule" collections
                if (email != null) {
                    // Get a reference to the Firebase Realtime Database
                    val database = Firebase.database

                    // Reference to "Users" and "JobSchedule" collections
                    val usersRef = database.getReference("Users")
                    val jobScheduleRef = database.getReference("JobSchedule")

                    // Remove the user data from "Users"
                    usersRef.orderByChild("email").equalTo(email)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    snapshot.ref.removeValue()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e("DeleteUser", "Failed to delete user: $databaseError")
                            }
                        })

                    // Remove the user data from "JobSchedule"
                    jobScheduleRef.orderByChild("email").equalTo(email)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot in dataSnapshot.children) {
                                    snapshot.ref.removeValue()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e("DeleteUser", "Failed to delete user from JobSchedule: $databaseError")
                            }
                        })

                    // You can also remove the user from your local list (mList) and notify the adapter to update
                    mList.removeAt(position)

                    notifyDataSetChanged()
                }
            }

            alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            alertDialog.show()
        }


    }


    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headingTextView: TextView = itemView.findViewById(R.id.headingTextView)
        val subHeadingTextView: TextView = itemView.findViewById(R.id.subHeadingTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val deleteButton : Button = itemView.findViewById(R.id.deleteButton)
    }
}
