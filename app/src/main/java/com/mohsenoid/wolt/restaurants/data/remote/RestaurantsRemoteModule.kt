package com.mohsenoid.wolt.restaurants.data.remote

import com.mohsenoid.wolt.BASE_URL_QUALIFIER
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal val restaurantsRemoteModule =
    module {
        single<Retrofit> {
            ApiServiceProvider.getRetrofit(get(named(BASE_URL_QUALIFIER)))
        }

        single<RestaurantsApiService> {
            val retrofit: Retrofit = get()
            retrofit.create(RestaurantsApiService::class.java)
        }
    }
