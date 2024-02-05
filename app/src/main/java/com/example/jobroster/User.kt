package com.example.jobroster
data class User(
    var name: String? = null,
    var email: String? = null,
    var dob: String? = null,
    var role: String? = null,
    var gender: String? = null,
    var totalHours : Long? = 0,
    var profilePicture : String? = null
) {
    // Default (no-argument) constructor
    constructor() : this(null,  null, null, null, null)
}
