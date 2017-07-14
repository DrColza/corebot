package com.gatehill.corebot.plugin

import com.gatehill.corebot.plugin.config.PluginSettings
import com.gatehill.corebot.plugin.model.PluginWrapper
import com.gatehill.corebot.store.DataStoreModule
import com.gatehill.corebot.util.yamlMapper
import com.gatehill.dlcl.classloader.ChildFirstDownloadingClassLoader
import com.gatehill.dlcl.exclusion
import com.gatehill.dlcl.jcenter
import com.gatehill.dlcl.jitpack
import com.gatehill.dlcl.mavenCentral
import com.google.inject.Module

// TODO move this into plugins config file
private val repos = listOf(
        mavenCentral,
        jcenter,
        jitpack,
        "exposed" to "https://dl.bintray.com/kotlin/exposed",
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

        val classLoader = ChildFirstDownloadingClassLoader(
                PluginSettings.localRepo, repos, PluginService::class.java.classLoader)

        DataStoreModule.storeClassLoaders += classLoader

        val pluginConfig = fetchPluginConfig()

        return mutableListOf<Module>().apply {
            // frontends and backends are instantiated the same way
            addAll(pluginConfig.frontends.union(pluginConfig.backends).flatMap { (dependency, classes) ->
                classLoader.fetch(dependency, excludes)

                classes.map { className ->
                    @Suppress("UNCHECKED_CAST")
                    val moduleClass: Class<Module> = classLoader.loadClass(className) as Class<Module>
                    moduleClass.newInstance()
                }
            })

            // stores must be instantiated with a name
            pluginConfig.storage?.let { (dependencies, stores) ->
                dependencies.map { classLoader.fetch(it, excludes) }
                addAll(stores.map { DataStoreModule(it) })
            }
        }
    }

    private fun fetchPluginConfig(): PluginWrapper = PluginSettings.pluginsFile?.let {
        @Suppress("UNCHECKED_CAST")
        yamlMapper.readValue(it.toFile(), PluginWrapper::class.java)
    }!!
}
