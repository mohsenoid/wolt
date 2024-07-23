package com.mohsenoid.wolt.restaurants.data.remote

import com.mohsenoid.wolt.restaurants.data.remote.model.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RestaurantsApiService {
    @GET("v1/pages/restaurants")
    suspend fun getRestaurants(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<RestaurantsResponse>
}
