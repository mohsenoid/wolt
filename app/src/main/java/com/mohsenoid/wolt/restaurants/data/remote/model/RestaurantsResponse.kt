package com.mohsenoid.wolt.restaurants.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class RestaurantsResponse(
    @SerializedName("sections")
    val sections: List<SectionRemoteModel>,
)
