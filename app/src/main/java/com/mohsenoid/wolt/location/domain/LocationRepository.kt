package com.mohsenoid.wolt.location.domain

import com.mohsenoid.wolt.location.domain.model.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Location>
}
