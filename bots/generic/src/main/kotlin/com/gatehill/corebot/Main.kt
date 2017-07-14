package com.gatehill.corebot

import com.gatehill.corebot.action.ActionFactoryConverter
import com.gatehill.corebot.action.NoOpActionFactoryConverter
import com.gatehill.corebot.plugin.PluginService
import com.google.inject.AbstractModule
import com.google.inject.Module

fun main(args: Array<String>) {
    val pluginService = PluginService()

    val modules = mutableListOf<Module>(BotModule())
    modules.addAll(pluginService.loadPluginInstances())

    Bot.build(*modules.toTypedArray()).start()
}

private class BotModule : AbstractModule() {
    override fun configure() {
        bind(BotBootstrap::class.java).asEagerSingleton()
        bind(ActionFactoryConverter::class.java).to(NoOpActionFactoryConverter::class.java).asSingleton()
    }
}
