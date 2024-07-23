package com.mohsenoid.wolt.restaurants.data

import com.mohsenoid.wolt.location.domain.model.Location
import com.mohsenoid.wolt.restaurants.data.db.dao.RestaurantsDao
import com.mohsenoid.wolt.restaurants.data.remote.RestaurantsApiService
import com.mohsenoid.wolt.restaurants.data.remote.model.RestaurantsResponse
import com.mohsenoid.wolt.restaurants.domain.RestaurantsRepository
import com.mohsenoid.wolt.util.createRestaurant
import com.mohsenoid.wolt.util.createRestaurantEntity
import com.mohsenoid.wolt.util.createRestaurantsResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.test.Test

class RestaurantsRepositoryImplTest {
    private lateinit var restaurantsApiService: RestaurantsApiService
    private lateinit var restaurantsDao: RestaurantsDao

    private lateinit var repository: RestaurantsRepository

    @Before
    fun setUp() {
        restaurantsApiService = mockk()
        restaurantsDao = mockk()

        repository =
            RestaurantsRepositoryImpl(
                restaurantsApiService = restaurantsApiService,
                restaurantsDao = restaurantsDao,
            )
    }

    @Test
    fun `Given location and API success response, When getRestaurants is called with same location, Then returns expected restaurants`() =
        runTest {
            // GIVEN
            val location = Location(lat = TEST_LOCATION_LAT, lon = TEST_LOCATION_LON)
            val restaurantsResponse: RestaurantsResponse =
                createRestaurantsResponse(
                    id = TEST_RESTAURANT_ID,
                    name = TEST_RESTAURANT_NAME,
                    shortDescription = TEST_RESTAURANT_SHORT_DESCRIPTION,
                    imageUrl = TEST_RESTAURANT_IMAGE_URL,
                )
            val expectedRestaurants =
                listOf(
                    createRestaurant(
                        id = TEST_RESTAURANT_ID,
                        name = TEST_RESTAURANT_NAME,
                        shortDescription = TEST_RESTAURANT_SHORT_DESCRIPTION,
                        imageUrl = TEST_RESTAURANT_IMAGE_URL,
                    ),
                )
            coEvery { restaurantsApiService.getRestaurants(lat = TEST_LOCATION_LAT, lon = TEST_LOCATION_LON) } returns Response.success(restaurantsResponse)
            coEvery { restaurantsDao.getFavouriteRestaurantsIds(setOf(TEST_RESTAURANT_ID)) } returns emptyList()

            // WHEN
            val actualRestaurants = repository.getRestaurants(location, 15).getOrNull()

            // THEN
            assertEquals(expectedRestaurants, actualRestaurants)
            coVerify(exactly = 1) { restaurantsApiService.getRestaurants(lat = any(), lon = any()) }
            coVerify(exactly = 1) { restaurantsDao.getFavouriteRestaurantsIds(ids = any()) }
        }

    @Test
    fun `Given location and API throws UnknownHostException, When getRestaurants is called with same location, Then returns failure`() =
        runTest {
            // GIVEN
            val location = Location(lat = TEST_LOCATION_LAT, lon = TEST_LOCATION_LON)
            coEvery { restaurantsApiService.getRestaurants(lat = TEST_LOCATION_LAT, lon = TEST_LOCATION_LON) } throws UnknownHostException()

            // WHEN
            val actualResponse = repository.getRestaurants(location, 15)

            // THEN
            assertFalse(actualResponse.isSuccess)
            coVerify(exactly = 1) { restaurantsApiService.getRestaurants(lat = any(), lon = any()) }
        }

    @Test
    fun `Given restaurants DAO upsert restaurant successfully, When upsertRestaurantFavourites is called, Then update result is success`() =
        runTest {
            // GIVEN
            val restaurantEntity = createRestaurantEntity(TEST_RESTAURANT_ID, true)
            coEvery { restaurantsDao.upsertRestaurant(restaurantEntity) } just runs

            // WHEN
            val updateResult = repository.upsertRestaurantFavourites(TEST_RESTAURANT_ID, isFavourite = true)

            // THEN
            assertTrue(updateResult.isSuccess)
            coVerify(exactly = 1) { restaurantsDao.upsertRestaurant(any()) }
        }

    @Test
    fun `Given restaurants DAO upsert restaurant throws exception, When upsertRestaurantFavourites is called, Then update result is failure`() =
        runTest {
            // GIVEN
            val restaurantEntity = createRestaurantEntity(TEST_RESTAURANT_ID, true)
            coEvery { restaurantsDao.upsertRestaurant(restaurantEntity) } throws IllegalArgumentException()

            // WHEN
            val updateResult = repository.upsertRestaurantFavourites(TEST_RESTAURANT_ID, isFavourite = true)

            // THEN
            assertFalse(updateResult.isSuccess)
            coVerify(exactly = 1) { restaurantsDao.upsertRestaurant(any()) }
        }

    companion object {
        private const val TEST_LOCATION_LAT = 20.10
        private const val TEST_LOCATION_LON = 30.10

        private const val TEST_RESTAURANT_ID = "1"
        private const val TEST_RESTAURANT_NAME = "Restaurant Name"
        private const val TEST_RESTAURANT_SHORT_DESCRIPTION = "Restaurant Short Description"
        private const val TEST_RESTAURANT_IMAGE_URL = "Restaurant Image URL"
    }
}
