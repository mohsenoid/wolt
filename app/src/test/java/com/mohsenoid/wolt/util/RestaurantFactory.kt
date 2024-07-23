package com.mohsenoid.wolt.util

import com.mohsenoid.wolt.restaurants.domain.model.Restaurant

fun createRestaurant(
    id: String = "Restaurant ID",
    name: String = "Restaurant Name",
    shortDescription: String = "Restaurant Short Description",
    imageUrl: String = "Restaurant Image URL",
    isFavourite: Boolean = false,
) = Restaurant(
    id = id,
    name = name,
    shortDescription = shortDescription,
    imageUrl = imageUrl,
    isFavourite = isFavourite,
)
