package com.example.jobroster

data class JobSchedule(
    var email: String? = null,
    var monday: DaySchedule? = null,
    var tuesday: DaySchedule? = null,
    var wednesday: DaySchedule? = null,
    var thursday: DaySchedule? = null,
    var friday: DaySchedule? = null,
    var saturday: DaySchedule? = null,
    var sunday: DaySchedule? = null
) {
    // Default (no-argument) constructor
    constructor() : this(null, null, null, null, null, null, null, null)

    // Convenience methods to set day schedules
    fun setMonday(dayOfWeek: String, startTime: String, endTime: String) {
        monday = DaySchedule(dayOfWeek, startTime, endTime)
    }

    fun setTuesday(dayOfWeek: String, startTime: String, endTime: String) {
        tuesday = DaySchedule(dayOfWeek, startTime, endTime)
    }

    fun setWednesday(dayOfWeek: String, startTime: String, endTime: String) {
        wednesday = DaySchedule(dayOfWeek, startTime, endTime)
    }

    fun setThursday(dayOfWeek: String, startTime: String, endTime: String) {
        thursday = DaySchedule(dayOfWeek, startTime, endTime)
    }

    fun setFriday(dayOfWeek: String, startTime: String, endTime: String) {
        friday = DaySchedule(dayOfWeek, startTime, endTime)
    }

    fun setSaturday(dayOfWeek: String, startTime: String, endTime: String) {
        saturday = DaySchedule(dayOfWeek, startTime, endTime)
    }

    fun setSunday(dayOfWeek: String, startTime: String, endTime: String) {
        sunday = DaySchedule(dayOfWeek, startTime, endTime)
    }
}
