package com.gatehill.corebot

import com.gatehill.corebot.chat.ActionTemplateConverter
import com.gatehill.corebot.chat.NoOpActionTemplateConverter
import com.gatehill.corebot.plugin.PluginService
import com.google.inject.AbstractModule
import com.google.inject.Module

fun main(args: Array<String>) {
    val modules = mutableListOf<Module>(BotModule())

    val pluginService = PluginService()
    modules.addAll(pluginService.loadPluginInstances())

    Bot.build(*modules.toTypedArray()).start()
}

private class BotModule : AbstractModule() {
    override fun configure() {
        bind(BotBootstrap::class.java).asEagerSingleton()
        bind(ActionTemplateConverter::class.java).to(NoOpActionTemplateConverter::class.java).asSingleton()
    }
}
