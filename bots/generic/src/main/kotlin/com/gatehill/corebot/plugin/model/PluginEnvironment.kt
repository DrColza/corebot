package com.gatehill.corebot.plugin.model

/**
 *
 * @author pete
 */
data class PluginEnvironment(val repositories: Map<String, String> = emptyMap(),
                             val exclusions: List<ExclusionEntry> = emptyList())
