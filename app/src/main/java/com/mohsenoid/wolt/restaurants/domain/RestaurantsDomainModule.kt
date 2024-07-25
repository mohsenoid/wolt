package com.mohsenoid.wolt.restaurants.domain

import com.mohsenoid.wolt.restaurants.domain.usecase.ObserverNearbyRestaurantsUseCase
import com.mohsenoid.wolt.restaurants.domain.usecase.UpsertFavouriteRestaurantUseCase
import org.koin.dsl.module

internal val restaurantsDomainModule =
    module {
        factory {
            ObserverNearbyRestaurantsUseCase(
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
