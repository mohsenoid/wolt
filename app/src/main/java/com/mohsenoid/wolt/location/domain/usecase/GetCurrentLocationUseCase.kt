package com.mohsenoid.wolt.location.domain.usecase

import com.mohsenoid.wolt.location.domain.LocationRepository
import com.mohsenoid.wolt.location.domain.NoLocationException
import com.mohsenoid.wolt.location.domain.model.Location

class GetCurrentLocationUseCase(private val locationRepository: LocationRepository) {
    suspend operator fun invoke(): Result {
        return locationRepository.getCurrentLocation().fold(
            onSuccess = { location ->
                Result.Success(location)
            },
            onFailure = { exception ->
                return when (exception) {
                    is NoLocationException -> Result.Failure.NoLocation
                    else -> Result.Failure.Unknown(exception.message ?: "Unknown Error")
                }
            },
        )
    }

    sealed interface Result {
        data class Success(val location: Location) : Result

        sealed interface Failure : Result {
            data object NoLocation : Result

            data class Unknown(val message: String) : Result
        }
    }
}
