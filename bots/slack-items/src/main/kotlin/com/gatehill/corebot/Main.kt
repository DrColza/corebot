package com.gatehill.corebot

import com.gatehill.corebot.action.ActionFactoryConverter
import com.gatehill.corebot.action.NoOpActionFactoryConverter
import com.gatehill.corebot.driver.items.ItemsDriverModule
import com.google.inject.AbstractModule

fun main(args: Array<String>) {
    Bot.build(BotModule(), SlackModule()).start()
}

private class BotModule : AbstractModule() {
    override fun configure() {
        bind(BotBootstrap::class.java).asEagerSingleton()
        bind(ActionFactoryConverter::class.java).to(NoOpActionFactoryConverter::class.java).asSingleton()

        // drivers
        install(ItemsDriverModule())
    }
}
