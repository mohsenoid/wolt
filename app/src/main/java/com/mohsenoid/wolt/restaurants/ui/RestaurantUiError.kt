package com.mohsenoid.wolt.restaurants.ui

sealed interface RestaurantUiError {
    data object NoInternetConnection : RestaurantUiError

    data object NoLocation : RestaurantUiError

    data class Unknown(val message: String) : RestaurantUiError
}
