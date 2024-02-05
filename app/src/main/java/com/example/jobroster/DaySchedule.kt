package com.example.jobroster

data class DaySchedule(
    var dayOfWeek: String? = null,
    var startTime: String? = null,
    var endTime: String? = null
) {
    // Default (no-argument) constructor
    constructor() : this(null, null, null)
}