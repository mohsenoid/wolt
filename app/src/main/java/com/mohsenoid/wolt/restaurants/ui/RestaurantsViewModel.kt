package com.mohsenoid.wolt.restaurants.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsenoid.wolt.restaurants.domain.usecase.ObserverNearbyRestaurantsUseCase
import com.mohsenoid.wolt.restaurants.domain.usecase.UpsertFavouriteRestaurantUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestaurantsViewModel(
    private val observerNearbyRestaurantsUseCase: ObserverNearbyRestaurantsUseCase,
    private val upsertFavouriteRestaurantUseCase: UpsertFavouriteRestaurantUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<RestaurantsUiState>(RestaurantsUiState.Loading)
    val uiState: StateFlow<RestaurantsUiState> = _uiState.asStateFlow()

    private val _errorEvent = MutableSharedFlow<RestaurantUiError>()
    val errorEvent: SharedFlow<RestaurantUiError> = _errorEvent.asSharedFlow()

    private var observeNearbyRestaurantsJob: Job? = null

    fun startObservingRestaurants() {
        observeNearbyRestaurantsJob?.cancel()

        observeNearbyRestaurantsJob =
            viewModelScope.launch {
                observerNearbyRestaurantsUseCase(
                    INTERVAL,
                    RESTAURANTS_LIMIT,
                ).collectLatest { result ->
                    when (result) {
                        is ObserverNearbyRestaurantsUseCase.Result.Success -> {
                            _uiState.value = RestaurantsUiState.Success(result.restaurants)
                        }

                        is ObserverNearbyRestaurantsUseCase.Result.Failure -> {
                            handleNearbyRestaurantsFailureResult(result)
                        }
                    }
                }
            }
    }

    private fun handleNearbyRestaurantsFailureResult(failure: ObserverNearbyRestaurantsUseCase.Result.Failure) {
        val error =
            when (failure) {
                ObserverNearbyRestaurantsUseCase.Result.Failure.NoInternetConnection -> {
                    RestaurantUiError.NoInternetConnection
                }
                ObserverNearbyRestaurantsUseCase.Result.Failure.NoLocation -> {
                    RestaurantUiError.NoLocation
                }
                is ObserverNearbyRestaurantsUseCase.Result.Failure.Unknown -> {
                    RestaurantUiError.Unknown(failure.message)
                }
            }

        viewModelScope.launch {
            _errorEvent.emit(error)
        }
    }

    fun stopObservingRestaurants() {
        observeNearbyRestaurantsJob?.cancel()
    }

    fun updateFavouriteRestaurant(
        id: String,
        isFavourite: Boolean,
    ) {
        viewModelScope.launch {
            when (val result = upsertFavouriteRestaurantUseCase(id, isFavourite)) {
                UpsertFavouriteRestaurantUseCase.Result.Success -> {
                    _uiState.update {
                        val restaurants =
                            (it as? RestaurantsUiState.Success)?.restaurants ?: return@launch
                        RestaurantsUiState.Success(
                            restaurants.map { restaurant ->
                                if (restaurant.id == id) {
                                    restaurant.copy(isFavourite = isFavourite)
                                } else {
                                    restaurant
                                }
                            },
                        )
                    }
                }

                is UpsertFavouriteRestaurantUseCase.Result.Failure -> {
                    viewModelScope.launch {
                        _errorEvent.emit(RestaurantUiError.Unknown("Unknown Error: ${result.message}"))
                    }
                }
            }
        }
    }

    companion object {
        private const val INTERVAL = 10_000L
        private const val RESTAURANTS_LIMIT = 15
    }
}
