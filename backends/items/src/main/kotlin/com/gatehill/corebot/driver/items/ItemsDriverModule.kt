package com.gatehill.corebot.driver.items

import com.gatehill.corebot.asSingleton
import com.gatehill.corebot.driver.items.service.ClaimService
import com.gatehill.corebot.store.DataStoreModule
import com.google.inject.AbstractModule

class ItemsDriverModule : AbstractModule() {
    override fun configure() {
        bind(ItemsDriverBootstrap::class.java).asEagerSingleton()

        // services
        bind(ClaimService::class.java).asSingleton()

        // data stores
        install(DataStoreModule("itemStore"))
    }
}
