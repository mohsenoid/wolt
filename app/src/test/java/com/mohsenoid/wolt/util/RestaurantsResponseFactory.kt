package com.mohsenoid.wolt.util

import com.mohsenoid.wolt.restaurants.data.remote.model.ImageRemoteModel
import com.mohsenoid.wolt.restaurants.data.remote.model.ItemRemoteModel
import com.mohsenoid.wolt.restaurants.data.remote.model.RestaurantsResponse
import com.mohsenoid.wolt.restaurants.data.remote.model.SectionRemoteModel
import com.mohsenoid.wolt.restaurants.data.remote.model.VenueRemoteModel

internal fun createRestaurantsResponse(
    id: String = "Restaurant Response ID",
    name: String = "Restaurant Response Name",
    shortDescription: String? = null,
    imageUrl: String = "Image URL",
): RestaurantsResponse {
    val venue = VenueRemoteModel(
        id = id,
        name = name,
        shortDescription = shortDescription,
    )
    val image = ImageRemoteModel(
        url = imageUrl,
    )
    val items = listOf(
        ItemRemoteModel(
            image = image,
            venue = venue,
        ),
    )
    val sections = listOf(
        SectionRemoteModel(
            name = "restaurants-delivering-venues",
            items = items,
        ),
    )
    return RestaurantsResponse(
        sections = sections,
    )
}
