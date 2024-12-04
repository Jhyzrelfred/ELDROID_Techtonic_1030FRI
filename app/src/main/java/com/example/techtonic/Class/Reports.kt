package com.example.techtonic.Class


data class Reports(
    val hazardType: String,
    val location: String,
    val status: String,
    val imageResource: Int,
    val description: String,
    val imageUrl: String
)
