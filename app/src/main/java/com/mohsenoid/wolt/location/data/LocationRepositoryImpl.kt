package com.mohsenoid.wolt.location.data

import com.mohsenoid.wolt.location.data.provider.LocationProvider
import com.mohsenoid.wolt.location.domain.LocationRepository
import com.mohsenoid.wolt.location.domain.NoLocationException
import com.mohsenoid.wolt.location.domain.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class LocationRepositoryImpl(
    private val locationProvider: LocationProvider,
) : LocationRepository {
    override suspend fun getCurrentLocation(): Result<Location> =
        withContext(Dispatchers.IO) {
            val location = locationProvider.getCurrentLocation()
            if (location != null) {
                Result.success(location)
            } else {
                Result.failure(NoLocationException("Error getting current location!"))
            }
        }
}
