package com.gatehill.corebot

import com.gatehill.corebot.action.ActionFactoryConverter
import com.gatehill.corebot.action.LockService
import com.gatehill.corebot.driver.jenkins.JenkinsDriverModule
import com.gatehill.corebot.driver.jobs.action.TriggerActionFactoryConverter
import com.gatehill.corebot.driver.rundeck.RundeckDriverModule
import com.gatehill.corebot.store.DataStoreModule
import com.google.inject.AbstractModule

fun main(args: Array<String>) {
    Bot.build(BotModule(), SlackModule()).start()
}

private class BotModule : AbstractModule() {
    override fun configure() {
        bind(BotBootstrap::class.java).asEagerSingleton()
        bind(LockService::class.java).asSingleton()
        bind(ActionFactoryConverter::class.java).to(TriggerActionFactoryConverter::class.java).asSingleton()

        // data stores
        install(DataStoreModule("lockStore"))

        // drivers
        install(JenkinsDriverModule())
        install(RundeckDriverModule())
    }
}
