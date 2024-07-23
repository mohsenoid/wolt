package com.mohsenoid.wolt.restaurants.data.mapper

import com.mohsenoid.wolt.restaurants.data.remote.model.ItemRemoteModel
import com.mohsenoid.wolt.restaurants.domain.model.Restaurant

internal object RestaurantMapper {
    fun ItemRemoteModel.toRestaurant(isFavourite: Boolean) =
        Restaurant(
            id = this.venue.id,
            name = this.venue.name,
            shortDescription = this.venue.shortDescription ?: "",
            imageUrl = this.image.url,
            isFavourite = isFavourite,
        )
}
