package com.mohsenoid.wolt.restaurants.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class ImageRemoteModel(
    @SerializedName("url")
    val url: String,
)
