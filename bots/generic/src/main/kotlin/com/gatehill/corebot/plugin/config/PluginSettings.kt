package com.gatehill.corebot.plugin.config

import java.nio.file.Path
import java.nio.file.Paths

object PluginSettings {
    val pluginsFile: Path? by lazy { System.getenv("PLUGIN_CONFIG_FILE")?.let { Paths.get(it) } }
    val localRepo: String by lazy { System.getenv("PLUGIN_LOCAL_REPO") ?: "target/local-repo" }
}
