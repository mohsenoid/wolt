package com.mohsenoid.wolt.restaurants.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohsenoid.wolt.restaurants.domain.model.Restaurant
import com.mohsenoid.wolt.ui.theme.WoltTheme
import com.mohsenoid.wolt.ui.util.AsyncImageWithPreview
import com.mohsenoid.wolt.ui.util.LoadingScreen
import com.mohsenoid.wolt.ui.util.SeparatorLine
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun RestaurantsScreen(modifier: Modifier = Modifier) {
    val viewModel: RestaurantsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle error messages
    LaunchedEffect(Unit) {
        viewModel.updateStatusError.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Fetch restaurants on launch
    LifecycleResumeEffect(Unit) {
        viewModel.startObservingRestaurants()
        onPauseOrDispose {
            viewModel.stopObservingRestaurants()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val currentUiState = uiState) {
            RestaurantsUiState.Loading -> LoadingScreen()
            is RestaurantsUiState.Success ->
                RestaurantsList(
                    restaurants = currentUiState.restaurants,
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

@Composable
fun RestaurantsList(
    restaurants: List<Restaurant>,
    onFavoriteClicked: (Restaurant) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = restaurants, key = { it.id }) { restaurant ->
            RestaurantItem(restaurant, onFavoriteClicked)
            SeparatorLine(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 8.dp, end = 24.dp),
                color = MaterialTheme.colorScheme.inverseOnSurface,
            )
        }
    }
}

@Composable
fun RestaurantItem(
    restaurant: Restaurant,
    onFavoriteClicked: (Restaurant) -> Unit,
) {
    Row(
        modifier =
            Modifier.height(80.dp)
                .fillMaxWidth()
                .clickable { onFavoriteClicked(restaurant) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RestaurantItemImage(restaurant)
        RestaurantItemInfo(restaurant, modifier = Modifier.weight(1f))
        RestaurantItemFavoriteIcon(isFavorite = restaurant.isFavourite)
    }
}

@Composable
private fun RestaurantItemImage(restaurant: Restaurant) {
    AsyncImageWithPreview(
        model = restaurant.imageUrl,
        contentDescription = restaurant.name,
        modifier =
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun RestaurantItemInfo(
    restaurant: Restaurant,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxHeight()
                .padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = restaurant.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = restaurant.shortDescription,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun RestaurantItemFavoriteIcon(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
        contentDescription = "Favourite",
        modifier =
            modifier
                .size(48.dp)
                .padding(8.dp),
    )
}

@Suppress("MagicNumber")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RestaurantItemPreview() {
    WoltTheme {
        RestaurantItem(
            restaurant =
                Restaurant(
                    id = "1",
                    name = "Burger Palace",
                    shortDescription = "A nice restaurant",
                    imageUrl = "https://example.com/image.jpg",
                    isFavourite = false,
                ),
            onFavoriteClicked = {},
        )
    }
}

@Suppress("MagicNumber")
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RestaurantListPreview() {
    WoltTheme {
        RestaurantsList(
            restaurants =
                listOf(
                    Restaurant(
                        id = "1",
                        name = "Burger Palace",
                        shortDescription = "A nice restaurant",
                        imageUrl = "https://example.com/image1.jpg",
                        isFavourite = false,
                    ),
                    Restaurant(
                        id = "2",
                        name = "Pizza Palace",
                        shortDescription = "Another nice restaurant",
                        imageUrl = "https://example.com/image2.jpg",
                        isFavourite = true,
                    ),
                ),
            onFavoriteClicked = {},
        )
    }
}
