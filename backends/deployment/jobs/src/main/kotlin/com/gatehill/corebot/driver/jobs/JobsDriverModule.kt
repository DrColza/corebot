package com.gatehill.corebot.driver.jobs

import com.gatehill.corebot.action.LockService
import com.gatehill.corebot.asSingleton
import com.gatehill.corebot.store.DataStoreModule
import com.google.inject.AbstractModule

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
class JobsDriverModule : AbstractModule() {
    override fun configure() {
        bind(JobsDriverBootstrap::class.java).asEagerSingleton()

        // services
        bind(LockService::class.java).asSingleton()

        // data stores
        install(DataStoreModule("lockStore"))
    }
}
