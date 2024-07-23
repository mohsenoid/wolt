package com.mohsenoid.wolt.restaurants.ui

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val restaurantsUiModule =
    module {
        viewModelOf(::RestaurantsViewModel)
    }
