package com.mohsenoid.wolt.restaurants.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohsenoid.wolt.restaurants.data.db.dao.RestaurantsDao
import com.mohsenoid.wolt.restaurants.data.db.entity.RestaurantEntity

@Database(
    entities = [RestaurantEntity::class],
    version = 1,
    exportSchema = true,
)
internal abstract class Database : RoomDatabase() {
    abstract fun restaurantsDao(): RestaurantsDao
}
