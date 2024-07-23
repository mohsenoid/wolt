package com.mohsenoid.wolt.restaurants

import com.mohsenoid.wolt.restaurants.data.restaurantsDataModules
import com.mohsenoid.wolt.restaurants.domain.restaurantsDomainModule
import com.mohsenoid.wolt.restaurants.ui.restaurantsUiModule

val restaurantsModules = restaurantsUiModule + restaurantsDomainModule + restaurantsDataModules
