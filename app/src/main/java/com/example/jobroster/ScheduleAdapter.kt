package com.example.jobroster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class ScheduleAdapter(private val supportFragmentManager: FragmentManager, private val mList: List<DaySchedule>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.schedulecard, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val daySchedule = mList[position]
        holder.dayOfWeek.text = daySchedule?.dayOfWeek
        holder.startTimeTextView.text = daySchedule?.startTime
        holder.endTimeTextView.text = daySchedule?.endTime


        holder.startTimeEditButton.setOnClickListener(){
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                // Handle the selected time
                val selectedHour = timePicker.hour
                val selectedMinute = timePicker.minute
                val selectedTimeText = String.format(
                    "%02d:%02d %s",
                    if (selectedHour > 12) selectedHour - 12 else selectedHour,
                    selectedMinute,
                    if (selectedHour >= 12) "PM" else "AM"
                )

                // Update the TextView with the selected time
                holder.startTimeTextView.text = selectedTimeText
            }

            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }

        holder.endTimeEditButton.setOnClickListener(){
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select Appointment time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                // Handle the selected time
                val selectedHour = timePicker.hour
                val selectedMinute = timePicker.minute
                val selectedTimeText = String.format(
                    "%02d:%02d %s",
                    if (selectedHour > 12) selectedHour - 12 else selectedHour,
                    selectedMinute,
                    if (selectedHour >= 12) "PM" else "AM"
                )

                // Update the TextView with the selected time
                holder.endTimeTextView.text = selectedTimeText
            }

            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }


    }


    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfWeek: TextView = itemView.findViewById(R.id.dayOfWeekTextView)
        val startTimeTextView: TextView = itemView.findViewById(R.id.startTimeTextView)
        val endTimeTextView: TextView = itemView.findViewById(R.id.endTimeTextView)
        val startTimeEditButton: ImageView  = itemView.findViewById(R.id.startTimeEdit)
        val endTimeEditButton: ImageView  = itemView.findViewById(R.id.endTimeEdit)
    }
}