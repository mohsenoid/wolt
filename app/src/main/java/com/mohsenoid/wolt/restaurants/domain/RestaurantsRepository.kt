package com.mohsenoid.wolt.restaurants.domain

import com.mohsenoid.wolt.location.domain.model.Location
import com.mohsenoid.wolt.restaurants.domain.model.Restaurant

interface RestaurantsRepository {
    suspend fun getRestaurants(
        location: Location,
        limit: Int,
    ): Result<List<Restaurant>>

    suspend fun upsertRestaurantFavourites(
        id: String,
        isFavourite: Boolean,
    ): Result<Unit>
}
