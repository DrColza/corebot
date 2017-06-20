package com.gatehill.corebot.plugin.model

/**
 *
 * @author pete
 */
data class PluginEntry(val dependency: String,
                       val classes: List<String> = emptyList())
