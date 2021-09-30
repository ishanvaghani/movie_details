package com.example.reviewz.UI.Fragment

data class Genre(
    val name: String,
    val id: Int
)

val movieGenres =
    listOf(
        Genre("Action", 28),
        Genre("Romance", 10749),
        Genre("Comedy", 35),
        Genre("Science Fiction", 878),
    )

val tvShowGenres =
    listOf(
        Genre("Crime", 80),
        Genre("Drama", 18),
        Genre("Mystery", 9648),
        Genre("Family", 10751),
    )