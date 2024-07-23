package com.mohsenoid.wolt.restaurants.domain.usecase

import com.mohsenoid.wolt.restaurants.domain.RestaurantsRepository

class UpsertFavouriteRestaurantUseCase(
    private val restaurantsRepository: RestaurantsRepository,
) {
    suspend operator fun invoke(
        id: String,
        isFavourite: Boolean,
    ): Result {
        return restaurantsRepository.upsertRestaurantFavourites(id, isFavourite).fold(
            onSuccess = {
                Result.Success
            },
            onFailure = { exception ->
                Result.Failure(exception.message ?: "Unknown Error")
            },
        )
    }

    sealed interface Result {
        data object Success : Result

        data class Failure(val message: String) : Result
    }
}
