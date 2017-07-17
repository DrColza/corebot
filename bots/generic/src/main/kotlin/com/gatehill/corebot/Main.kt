package com.gatehill.corebot

import com.gatehill.corebot.action.ActionFactoryConverter
import com.gatehill.corebot.action.NoOpActionFactoryConverter
import com.gatehill.corebot.plugin.PluginService
import com.gatehill.corebot.plugin.config.PluginSettings
import com.google.inject.AbstractModule
import com.google.inject.Module
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val pluginService = PluginService()

    when (args.getOrNull(0)) {
        "download" -> downloadPlugins(pluginService)
        "run" -> runBot(pluginService)
        else -> {
            System.err.println("No command specified - try 'download' or 'run'.")
            exitProcess(1)
        }
    }
}

private fun downloadPlugins(pluginService: PluginService) {
    println("Downloading plugins in ${PluginSettings.pluginsFile}")
    pluginService.fetchPlugins()
    println("Plugins downloaded to ${PluginSettings.localRepo}")
}

private fun runBot(pluginService: PluginService) {
    println("Loading plugins from ${PluginSettings.localRepo}")
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
