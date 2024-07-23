package com.mohsenoid.wolt.restaurants.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class ItemRemoteModel(
    @SerializedName("image")
    val image: ImageRemoteModel,
    @SerializedName("venue")
    val venue: VenueRemoteModel,
)
