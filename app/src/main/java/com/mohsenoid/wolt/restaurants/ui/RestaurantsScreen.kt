package com.mohsenoid.wolt.restaurants.ui

import android.content.res.Configuration
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohsenoid.wolt.restaurants.domain.model.Restaurant
import com.mohsenoid.wolt.ui.theme.WoltTheme
import com.mohsenoid.wolt.ui.util.AsyncImageWithPreview
import com.mohsenoid.wolt.ui.util.LoadingScreen
import com.mohsenoid.wolt.ui.util.SeparatorLine

@Composable
fun RestaurantsScreen(
    uiState: RestaurantsUiState,
    modifier: Modifier = Modifier,
    onFavoriteClicked: (Restaurant) -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        when (uiState) {
            RestaurantsUiState.Loading -> {
                LoadingScreen()
            }

            is RestaurantsUiState.Success -> {
                RestaurantsList(
                    onFavoriteClicked = { restaurant ->
                        onFavoriteClicked(restaurant)
                    },
                    restaurants = uiState.restaurants,
                )
            }
        }
    }
}

@Composable
fun RestaurantsList(
    modifier: Modifier = Modifier,
    onFavoriteClicked: (Restaurant) -> Unit = {},
    restaurants: List<Restaurant>,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = restaurants,
            key = { it.id },
        ) { restaurant ->
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
    onFavoriteClicked: (Restaurant) -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .height(80.dp)
                .fillMaxWidth()
                .clickable { onFavoriteClicked(restaurant) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
        Column(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .weight(1f),
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
        Icon(
            imageVector = if (restaurant.isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
            contentDescription = "Favourite",
            modifier =
                Modifier
                    .size(48.dp)
                    .padding(8.dp),
        )
    }
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
        )
    }
}
