package com.mohsenoid.wolt.restaurants.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class SectionRemoteModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("items")
    val items: List<ItemRemoteModel>,
)
