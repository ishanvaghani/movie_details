package com.example.reviewz.Model

import com.google.gson.annotations.SerializedName

data class Movie(
    val title: String,
    val name: String,
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String
)