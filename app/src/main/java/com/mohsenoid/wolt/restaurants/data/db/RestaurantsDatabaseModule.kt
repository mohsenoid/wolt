package com.mohsenoid.wolt.restaurants.data.db

import org.koin.dsl.module

internal val restaurantsDatabaseModule =
    module {
        single {
            DatabaseProvider.getDatabase(context = get())
        }

        factory {
            val db: Database = get()
            db.restaurantsDao()
        }
    }
