package com.mohsenoid.wolt

import com.mohsenoid.wolt.location.locationModules
import com.mohsenoid.wolt.restaurants.restaurantsModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

@Suppress("TopLevelPropertyNaming")
const val BASE_URL_QUALIFIER = "BASE_URL"

private val appModule =
    module {
        single(named(BASE_URL_QUALIFIER)) {
            BuildConfig.API_BASE_URL
        }
    }

val appModules = appModule + restaurantsModules + locationModules
