package com.example.techtonic.Class

data class NotificationClass(
    val id: String,
    val hazardType: String,
    val status: String,
    var isRead: Boolean,
    val imageUrl: String
)