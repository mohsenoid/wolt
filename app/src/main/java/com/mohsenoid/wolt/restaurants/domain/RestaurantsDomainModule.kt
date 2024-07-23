package com.mohsenoid.wolt.restaurants.domain

import com.mohsenoid.wolt.restaurants.domain.usecase.GetRestaurantsUseCase
import com.mohsenoid.wolt.restaurants.domain.usecase.UpsertFavouriteRestaurantUseCase
import org.koin.dsl.module

internal val restaurantsDomainModule =
    module {
        factory {
            GetRestaurantsUseCase(
                getCurrentLocationUseCase = get(),
                restaurantsRepository = get(),
            )
        }

        factory {
            UpsertFavouriteRestaurantUseCase(
                restaurantsRepository = get(),
            )
        }
    }
