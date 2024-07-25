package com.mohsenoid.wolt

import com.mohsenoid.wolt.location.domain.usecase.GetCurrentLocationUseCase
import com.mohsenoid.wolt.restaurants.data.restaurantsDataModules
import com.mohsenoid.wolt.restaurants.domain.RestaurantsRepository
import com.mohsenoid.wolt.restaurants.domain.restaurantsDomainModule
import com.mohsenoid.wolt.restaurants.domain.usecase.ObserverNearbyRestaurantsUseCase
import com.mohsenoid.wolt.restaurants.domain.usecase.UpsertFavouriteRestaurantUseCase
import com.mohsenoid.wolt.restaurants.ui.restaurantsUiModule
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import org.koin.test.verify.verifyAll
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
class KoinModulesTest : KoinTest {
    @Test
    fun `Verify restaurants data modules`() {
        restaurantsDataModules.verifyAll()
    }

    @Test
    fun `Verify restaurants domain modules`() {
        restaurantsDomainModule.verify(
            extraTypes =
                listOf(
                    GetCurrentLocationUseCase::class,
                    RestaurantsRepository::class,
                ),
        )
    }

    @Test
    fun `Verify restaurants ui modules`() {
        restaurantsUiModule.verify(
            extraTypes =
                listOf(
                    ObserverNearbyRestaurantsUseCase::class,
                    UpsertFavouriteRestaurantUseCase::class,
                ),
        )
    }
}
