package com.mohsenoid.wolt.restaurants.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class VenueRemoteModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("short_description")
    val shortDescription: String?,
)
