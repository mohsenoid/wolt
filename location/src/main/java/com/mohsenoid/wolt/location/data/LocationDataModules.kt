package com.mohsenoid.wolt.location.data

import com.mohsenoid.wolt.location.data.provider.locationProviderModule
import com.mohsenoid.wolt.location.domain.LocationRepository
import org.koin.dsl.module

private val locationDataModule =
    module {
        single<LocationRepository> {
            LocationRepositoryImpl(
                locationProvider = get(),
            )
        }
    }

val locationDataModules = locationDataModule + locationProviderModule
