package com.mohsenoid.wolt.util

import com.mohsenoid.wolt.restaurants.data.db.entity.RestaurantEntity

internal fun createRestaurantEntity(
    id: String = "Restaurant ID",
    isFavourite: Boolean = false,
) = RestaurantEntity(
    id = id,
    isFavourite = isFavourite,
)
