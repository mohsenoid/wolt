package com.mohsenoid.wolt

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohsenoid.wolt.restaurants.ui.RestaurantsScreen
import com.mohsenoid.wolt.restaurants.ui.RestaurantsViewModel
import com.mohsenoid.wolt.ui.theme.WoltTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WoltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewModel: RestaurantsViewModel = koinViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    LaunchedEffect(Unit) {
                        viewModel.updateStatusError.collect { updateStatusError ->
                            Toast.makeText(
                                this@MainActivity,
                                updateStatusError,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }

                    LaunchedEffect(true) {
                        viewModel.getRestaurants()
                    }

                    RestaurantsScreen(
                        modifier = Modifier.padding(innerPadding),
                        uiState = uiState,
                        onFavoriteClicked = { restaurant ->
                            viewModel.updateFavouriteRestaurant(
                                id = restaurant.id,
                                isFavourite = !restaurant.isFavourite,
                            )
                        },
                    )
                }
            }
        }
    }
}
