package com.mohsenoid.wolt.restaurants.domain.usecase

import com.mohsenoid.wolt.location.domain.model.Location
import com.mohsenoid.wolt.location.domain.usecase.GetCurrentLocationUseCase
import com.mohsenoid.wolt.restaurants.domain.NoInternetConnectionException
import com.mohsenoid.wolt.restaurants.domain.RestaurantsRepository
import com.mohsenoid.wolt.restaurants.domain.model.Restaurant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserverNearbyRestaurantsUseCase(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val restaurantsRepository: RestaurantsRepository,
) {
    suspend operator fun invoke(
        interval: Long,
        limit: Int,
    ): Flow<Result> {
        return flow {
            while (true) {
                val result =
                    when (val locationResult = getCurrentLocationUseCase()) {
                        is GetCurrentLocationUseCase.Result.Success -> {
                            getRestaurants(locationResult.location, limit)
                        }

                        GetCurrentLocationUseCase.Result.Failure.NoLocation -> {
                            Result.Failure.NoLocation
                        }

                        is GetCurrentLocationUseCase.Result.Failure.Unknown -> {
                            Result.Failure.Unknown(locationResult.message)
                        }
                    }

                emit(result)

                delay(interval)
            }
        }
    }

    private suspend fun getRestaurants(
        location: Location,
        limit: Int,
    ): Result {
        return restaurantsRepository.getRestaurants(location, limit).fold(
            onSuccess = { restaurants ->
                Result.Success(restaurants)
            },
            onFailure = { exception ->
                when (exception) {
                    is NoInternetConnectionException -> Result.Failure.NoInternetConnection
                    else -> Result.Failure.Unknown(exception.message ?: "Unknown Error")
                }
            },
        )
    }

    sealed interface Result {
        data class Success(val restaurants: List<Restaurant>) : Result

        sealed interface Failure : Result {
            data object NoInternetConnection : Result

            data object NoLocation : Result

            data class Unknown(val message: String) : Result
        }
    }
}
