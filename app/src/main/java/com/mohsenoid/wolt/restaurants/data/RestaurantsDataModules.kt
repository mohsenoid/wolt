package com.mohsenoid.wolt.restaurants.data

import com.mohsenoid.wolt.restaurants.data.db.restaurantsDatabaseModule
import com.mohsenoid.wolt.restaurants.data.remote.restaurantsRemoteModule
import com.mohsenoid.wolt.restaurants.domain.RestaurantsRepository
import org.koin.dsl.module

private val restaurantsDataModule =
    module {
        single<RestaurantsRepository> {
            RestaurantsRepositoryImpl(
                restaurantsApiService = get(),
                restaurantsDao = get(),
            )
        }
    }

val restaurantsDataModules =
    restaurantsDataModule + restaurantsDatabaseModule + restaurantsRemoteModule
