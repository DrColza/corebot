package com.gatehill.corebot.plugin

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.gatehill.corebot.plugin.config.PluginSettings
import com.gatehill.corebot.plugin.model.Plugin
import com.gatehill.corebot.util.jsonMapper
import com.gatehill.mcl.ChildFirstDownloadingClassLoader
import com.gatehill.mcl.exclusion
import com.gatehill.mcl.jcenter
import com.gatehill.mcl.jitpack
import com.gatehill.mcl.mavenCentral
import com.google.inject.Module

val repos = listOf(
        mavenCentral,
        jcenter,
        jitpack,
        "gatehill" to "https://gatehillsoftware-maven.s3.amazonaws.com/snapshots/"
)

/**
 *
 * @author pete
 */
class PluginService {
    fun loadPluginInstances(): Collection<Module> {
        // TODO derive this from the engine and api modules' transitive dependencies
        val excludes = listOf(
                exclusion("org.jetbrains.kotlin", "kotlin-stdlib"),
                exclusion("org.jetbrains.kotlin", "kotlin-reflect"),
                exclusion("org.jetbrains", "annotations"),
                exclusion("javax.inject", "javax.inject"),
                exclusion("org.apache.logging.log4j", "log4j-api"),
                exclusion("com.google.inject", "guice"),
                exclusion("com.google.guava", "guava"),
                exclusion("com.fasterxml.jackson.module", "jackson-module-kotlin"),
                exclusion("com.fasterxml.jackson.core", "jackson-databind"),
                exclusion("com.fasterxml.jackson.core", "jackson-annotations"),
                exclusion("com.fasterxml.jackson.dataformat", "jackson-dataformat-yaml"),
                exclusion("com.fasterxml.jackson.core", "jackson-core"),
                exclusion("org.yaml", "snakeyaml"),
                exclusion("aopalliance", "aopalliance"),
                exclusion("ch.qos.logback", "logback-classic"),
                exclusion("org.slf4j", "slf4j-api"),
                exclusion("com.gatehill.corebot", "core-api"),
                exclusion("com.gatehill.corebot", "core-engine")
        )

        return fetchPluginConfig().flatMap { (dependency, classes) ->
            val classLoader = ChildFirstDownloadingClassLoader(
                    PluginSettings.localRepo, dependency, excludes, repos, this::class.java.classLoader)

            classes.map { className ->
                @Suppress("UNCHECKED_CAST")
                val moduleClass: Class<Module> = classLoader.loadClass(className) as Class<Module>
                moduleClass.newInstance()
            }
        }
    }

    private fun fetchPluginConfig(): List<Plugin> = PluginSettings.pluginsFile?.let {
        @Suppress("UNCHECKED_CAST")
        jsonMapper.readValue<List<Plugin>>(it.toFile(), jacksonTypeRef<List<Plugin>>())
    } ?: emptyList()
}
