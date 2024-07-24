package com.mohsenoid.wolt.restaurants.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mohsenoid.wolt.util.createRestaurant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class RestaurantsScreenTest {
    @get:Rule
    val rule = createComposeRule()

    @Before
    fun setUp() {
        stopKoin() // To fix KoinAppAlreadyStartedException
    }

    @After
    fun tearDown() {
        stopKoin() // To fix KoinAppAlreadyStartedException
    }

    @Test
    fun `Given a success RestaurantsUiState, When content is RestaurantsScreen, Then it should display restaurant name and short description`() {
        // GIVEN
        val restaurants =
            listOf(
                createRestaurant(
                    name = TEST_RESTAURANT_NAME,
                    shortDescription = TEST_RESTAURANT_SHORT_DESCRIPTION,
                ),
            )

        // WHEN
        rule.setContent {
            RestaurantsList(restaurants = restaurants, onFavoriteClicked = {})
        }

        // THEN
        rule.onNodeWithText(TEST_RESTAURANT_NAME).assertExists()
        rule.onNodeWithText(TEST_RESTAURANT_SHORT_DESCRIPTION).assertExists()
    }

    companion object {
        private const val TEST_RESTAURANT_NAME = "name"
        private const val TEST_RESTAURANT_SHORT_DESCRIPTION = "shortDescription"
    }
}
