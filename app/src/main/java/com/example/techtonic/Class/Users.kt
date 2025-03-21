package com.example.techtonic.models

data class Users(
    val email: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val phoneNumber: String? = "",
    val imageUrl: String? = ""
) {
    // No-argument constructor required by Firebase
    constructor() : this("", "", "", "", "")
}