package com.gatehill.corebot.plugin.model

/**
 *
 * @author pete
 */
data class PluginWrapper(val frontends: List<PluginEntry> = emptyList(),
                         val backends: List<PluginEntry> = emptyList(),
                         val stores: Map<String, PluginEntry> = emptyMap())
