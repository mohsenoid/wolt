package com.mohsenoid.wolt.location.data.provider

import com.mohsenoid.wolt.location.domain.model.Location

internal interface LocationProvider {
    fun getCurrentLocation(): Location?
}
