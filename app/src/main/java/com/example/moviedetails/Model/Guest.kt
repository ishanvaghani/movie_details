package com.example.moviedetails.Model

import com.google.gson.annotations.SerializedName

data class Guest(
    val success: Boolean,
    @SerializedName("guest_session_id")
    val guestSessionId: String
)