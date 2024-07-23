package com.mohsenoid.wolt.location.data.provider.mock

import com.mohsenoid.wolt.location.data.provider.LocationProvider
import com.mohsenoid.wolt.location.domain.model.Location

internal class MockLocationProvider : LocationProvider {
    private val coordinates =
        listOf(
            60.170187 to 24.930599,
            60.169418 to 24.931618,
            60.169818 to 24.932906,
            60.170005 to 24.935105,
            60.169108 to 24.936210,
            60.168355 to 24.934869,
            60.167560 to 24.932562,
            60.168254 to 24.931532,
            60.169012 to 24.930341,
            60.170085 to 24.929569,
        )

    private var currentIndex = 0

    override fun getCurrentLocation(): Location {
        val (lat, lon) = coordinates[currentIndex++ % coordinates.size]
        return Location(lat, lon)
    }
}
