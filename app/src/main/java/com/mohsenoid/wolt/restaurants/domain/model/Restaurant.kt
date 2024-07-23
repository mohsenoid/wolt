package com.mohsenoid.wolt.restaurants.domain.model

data class Restaurant(
    val id: String,
    val name: String,
    val shortDescription: String,
    val imageUrl: String,
    val isFavourite: Boolean,
)
