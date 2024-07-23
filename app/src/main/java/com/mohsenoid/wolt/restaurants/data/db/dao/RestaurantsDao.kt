package com.mohsenoid.wolt.restaurants.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mohsenoid.wolt.restaurants.data.db.entity.RestaurantEntity

@Dao
internal interface RestaurantsDao {
    @Upsert
    suspend fun upsertRestaurant(restaurant: RestaurantEntity)

    @Query("SELECT id FROM restaurant WHERE is_favourite = 1 AND id IN (:ids)")
    suspend fun getFavouriteRestaurantsIds(ids: Set<String>): List<String>
}
