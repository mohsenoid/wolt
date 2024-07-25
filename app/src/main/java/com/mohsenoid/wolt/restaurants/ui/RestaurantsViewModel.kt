package com.mohsenoid.wolt.restaurants.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsenoid.wolt.restaurants.domain.usecase.GetRestaurantsUseCase
import com.mohsenoid.wolt.restaurants.domain.usecase.UpsertFavouriteRestaurantUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestaurantsViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val upsertFavouriteRestaurantUseCase: UpsertFavouriteRestaurantUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<RestaurantsUiState> =
        MutableStateFlow(RestaurantsUiState.Loading)
    val uiState: StateFlow<RestaurantsUiState> by ::_uiState

    private val _updateStatusError: MutableSharedFlow<String> = MutableSharedFlow()
    val updateStatusError: Flow<String> by ::_updateStatusError

    private var getRestaurantsJob: Job? = null

    fun startObservingRestaurants() {
        getRestaurantsJob?.cancel()

        getRestaurantsJob =
            viewModelScope.launch {
                getRestaurantsUseCase(INTERVAL, RESTAURANTS_LIMIT).collectLatest { result ->
                    when (result) {
                        is GetRestaurantsUseCase.Result.Success -> {
                            _uiState.value = RestaurantsUiState.Success(result.restaurants)
                        }

                        GetRestaurantsUseCase.Result.Failure.NoInternetConnection -> {
                            _updateStatusError.emit("No Internet Connection")
                        }

                        GetRestaurantsUseCase.Result.Failure.NoLocation -> {
                            _updateStatusError.emit("No Location data")
                        }

                        is GetRestaurantsUseCase.Result.Failure.Unknown -> {
                            _updateStatusError.emit("Unknown Error: ${result.message}")
                        }
                    }
                }
            }
    }

    fun stopObservingRestaurants() {
        getRestaurantsJob?.cancel()
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
                    _updateStatusError.emit("Unknown Error: ${result.message}")
                }
            }
        }
    }

    companion object {
        private const val INTERVAL = 10_000L
        private const val RESTAURANTS_LIMIT = 15
    }
}
