package com.gatehill.corebot.plugin.model

/**
 *
 * @author pete
 */
data class StorageWrapper(val dependencies: List<String> = emptyList(),
                          val stores: List<String> = emptyList())
