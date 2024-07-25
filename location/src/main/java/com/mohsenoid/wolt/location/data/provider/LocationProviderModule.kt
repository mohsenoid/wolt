package com.mohsenoid.wolt.location.data.provider

import com.mohsenoid.wolt.location.data.provider.mock.MockLocationProvider
import org.koin.dsl.module

internal val locationProviderModule =
    module {
        factory<LocationProvider> {
            MockLocationProvider()
        }
    }
