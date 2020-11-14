package com.example.moviedetails.Model


import com.google.gson.annotations.SerializedName

data class TvShowDetails(
    val id: Int,
    val tagline: String,
    val name: String,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("first_air_date")
    val firstRelease: String,
    @SerializedName("last_air_date")
    val lastRelease: String,
    val seasons: List<Season>
) {
    data class Season(
        val id: Int,
        val name: String,
        @SerializedName("poster_path")
        val posterPath: String,
    )
}