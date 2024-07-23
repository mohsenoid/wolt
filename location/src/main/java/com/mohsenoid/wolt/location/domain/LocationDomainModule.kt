package com.mohsenoid.wolt.location.domain

import com.mohsenoid.wolt.location.domain.usecase.GetCurrentLocationUseCase
import org.koin.dsl.module

val locationDomainModule =
    module {
        factory {
            GetCurrentLocationUseCase(locationRepository = get())
        }
    }
