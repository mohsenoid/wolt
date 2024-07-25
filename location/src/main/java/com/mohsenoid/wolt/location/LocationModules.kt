package com.mohsenoid.wolt.location

import com.mohsenoid.wolt.location.data.locationDataModules
import com.mohsenoid.wolt.location.domain.locationDomainModule

val locationModules = locationDomainModule + locationDataModules
