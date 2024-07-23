package com.mohsenoid.wolt.restaurants.data

import com.mohsenoid.wolt.location.domain.model.Location
import com.mohsenoid.wolt.restaurants.data.db.dao.RestaurantsDao
import com.mohsenoid.wolt.restaurants.data.db.entity.RestaurantEntity
import com.mohsenoid.wolt.restaurants.data.mapper.RestaurantMapper.toRestaurant
import com.mohsenoid.wolt.restaurants.data.remote.RestaurantsApiService
import com.mohsenoid.wolt.restaurants.data.remote.model.ItemRemoteModel
import com.mohsenoid.wolt.restaurants.domain.NoInternetConnectionException
import com.mohsenoid.wolt.restaurants.domain.RestaurantsRepository
import com.mohsenoid.wolt.restaurants.domain.model.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class RestaurantsRepositoryImpl(
    private val restaurantsApiService: RestaurantsApiService,
    private val restaurantsDao: RestaurantsDao,
) : RestaurantsRepository {
    override suspend fun getRestaurants(
        location: Location,
        limit: Int,
    ): Result<List<Restaurant>> =
        withContext(Dispatchers.IO) {
            getRestaurantsFromRemote(location, limit)
        }

    private suspend fun getRestaurantsFromRemote(
        location: Location,
        limit: Int,
    ): Result<List<Restaurant>> =
        withContext(Dispatchers.IO) {
            try {
                val response = restaurantsApiService.getRestaurants(location.lat, location.lon)
                val restaurantsResponseItems: List<ItemRemoteModel>? =
                    response.body()?.sections?.firstOrNull { it.name == RESTAURANTS_SECTION_NAME }?.items
                if (response.isSuccessful && restaurantsResponseItems != null) {
                    handleSuccessfulRestaurantsResponse(restaurantsResponseItems.take(limit))
                } else {
                    Result.failure(Exception(response.message().ifEmpty { "Unknown Error" }))
                }
            } catch (e: UnknownHostException) {
                Result.failure(NoInternetConnectionException(e.message))
            } catch (e: SocketTimeoutException) {
                Result.failure(NoInternetConnectionException(e.message))
            }
        }

    private suspend fun handleSuccessfulRestaurantsResponse(restaurantsResponseItems: List<ItemRemoteModel>): Result<List<Restaurant>> {
        val restaurantsIds: Set<String> =
            restaurantsResponseItems.map { item -> item.venue.id }.toSet()
        val favouriteRestaurantsIds = restaurantsDao.getFavouriteRestaurantsIds(restaurantsIds)

        val restaurants =
            restaurantsResponseItems.map { item ->
                val isFavourite: Boolean = favouriteRestaurantsIds.contains(item.venue.id)
                item.toRestaurant(isFavourite)
            }

        return Result.success(restaurants)
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun upsertRestaurantFavourites(
        id: String,
        isFavourite: Boolean,
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val restaurantEntity = RestaurantEntity(id = id, isFavourite = isFavourite)
                restaurantsDao.upsertRestaurant(restaurantEntity)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    companion object {
        private const val RESTAURANTS_SECTION_NAME = "restaurants-delivering-venues"
    }
}
