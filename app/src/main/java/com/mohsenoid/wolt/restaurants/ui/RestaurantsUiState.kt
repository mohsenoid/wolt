package com.mohsenoid.wolt.restaurants.ui

import com.mohsenoid.wolt.restaurants.domain.model.Restaurant

sealed interface RestaurantsUiState {
    data object Loading : RestaurantsUiState

    data class Success(val restaurants: List<Restaurant>) : RestaurantsUiState
}
