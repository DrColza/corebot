package com.gatehill.corebot

import com.gatehill.corebot.action.ActionFactoryConverter
import com.gatehill.corebot.action.NoOpActionFactoryConverter
import com.gatehill.corebot.plugin.PluginService
import com.gatehill.corebot.plugin.config.PluginSettings
import com.google.inject.AbstractModule
import kotlin.system.exitProcess

const val usage = """Usage: ./bots-generic <commands>

Commands:
 clean    - clean the downloaded plugins
 download - download dependencies
 run      - run the bot using the downloaded dependencies
"""

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        exitWithMessage("No command specified\n$usage")

    } else {
        val pluginService = PluginService()

        args.forEach {
            when (it) {
                "clean" -> pluginService.clearRepo()
                "download" -> downloadPlugins(pluginService)
                "run" -> runBot(pluginService)
                else -> exitWithMessage("Unsupported command '$it'\n$usage")
            }
        }
    }
}

private fun exitWithMessage(message: String) {
    System.err.println(message)
    exitProcess(1)
}

private fun downloadPlugins(pluginService: PluginService) {
    println("Downloading plugins in ${PluginSettings.pluginsFile}")
    pluginService.fetchPlugins()
    println("Plugins downloaded to ${PluginSettings.localRepo}")
}

private fun runBot(pluginService: PluginService) {
    println("Loading plugins from ${PluginSettings.localRepo}")
    val modules = pluginService.loadPluginInstances() + BotModule()
    Bot.build(*modules.toTypedArray()).start()
}

private class BotModule : AbstractModule() {
    override fun configure() {
        bind(BotBootstrap::class.java).asEagerSingleton()
        bind(ActionFactoryConverter::class.java).to(NoOpActionFactoryConverter::class.java).asSingleton()
    }
}
