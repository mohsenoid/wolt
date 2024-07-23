package com.mohsenoid.wolt.restaurants.data.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mohsenoid.wolt.restaurants.data.db.Database
import com.mohsenoid.wolt.util.MainDispatcherRule
import com.mohsenoid.wolt.util.createRestaurantEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RestaurantsDaoTest {
    @get:Rule
    var rule: TestRule = MainDispatcherRule()

    private lateinit var db: Database
    private lateinit var restaurantsDao: RestaurantsDao

    @Before
    fun setUp() {
        stopKoin() // To fix KoinAppAlreadyStartedException

        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
        restaurantsDao = db.restaurantsDao()
    }

    @After
    fun tearDown() {
        db.close()
        stopKoin() // To fix KoinAppAlreadyStartedException
    }

    @Test
    fun `Given favourite restaurantEntities upserted, When getFavouriteRestaurantsIds called with expected ID, Then result should be expectedEntity`() =
        runTest {
            // Given
            val expectedEntity = createRestaurantEntity(id = TEST_EXPECTED_RESTAURANT_ID, isFavourite = true)
            val unexpectedEntity = createRestaurantEntity(id = TEST_UNEXPECTED_RESTAURANT_ID, isFavourite = true)
            restaurantsDao.upsertRestaurant(expectedEntity)
            restaurantsDao.upsertRestaurant(unexpectedEntity)

            // When
            val actualFavouriteRestaurantsIds = restaurantsDao.getFavouriteRestaurantsIds(setOf(TEST_EXPECTED_RESTAURANT_ID))

            // Then
            assertTrue(actualFavouriteRestaurantsIds.contains(TEST_EXPECTED_RESTAURANT_ID))
            assertFalse(actualFavouriteRestaurantsIds.contains(TEST_UNEXPECTED_RESTAURANT_ID))
        }

    @Test
    fun `Given restaurantEntities upserted, When upsertRestaurant with isFavourite true called, Then getFavouriteRestaurantsIds should include the expected ID`() =
        runTest {
            // Given
            val entity1 = createRestaurantEntity(id = TEST_EXPECTED_RESTAURANT_ID, isFavourite = false)
            val entity2 = createRestaurantEntity(id = TEST_UNEXPECTED_RESTAURANT_ID, isFavourite = false)
            restaurantsDao.upsertRestaurant(entity1)
            restaurantsDao.upsertRestaurant(entity2)

            // When
            val favouriteEntity1 = entity1.copy(isFavourite = true)
            restaurantsDao.upsertRestaurant(favouriteEntity1)
            val actualFavouriteRestaurantsIds = restaurantsDao.getFavouriteRestaurantsIds(setOf(TEST_EXPECTED_RESTAURANT_ID, TEST_UNEXPECTED_RESTAURANT_ID))

            // Then
            assertTrue(actualFavouriteRestaurantsIds.contains(TEST_EXPECTED_RESTAURANT_ID))
            assertFalse(actualFavouriteRestaurantsIds.contains(TEST_UNEXPECTED_RESTAURANT_ID))
        }

    companion object {
        private const val TEST_EXPECTED_RESTAURANT_ID = "Expected Restaurant ID"
        private const val TEST_UNEXPECTED_RESTAURANT_ID = "Unexpected Restaurant ID"
    }
}
