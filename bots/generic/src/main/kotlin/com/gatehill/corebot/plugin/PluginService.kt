package com.gatehill.corebot.plugin

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.gatehill.corebot.classloader.ClassLoaderUtil
import com.gatehill.corebot.plugin.config.PluginSettings
import com.gatehill.corebot.plugin.model.PluginEnvironment
import com.gatehill.corebot.plugin.model.PluginWrapper
import com.gatehill.corebot.util.yamlMapper
import com.gatehill.dlcl.Collector
import com.gatehill.dlcl.Downloader
import com.gatehill.dlcl.classloader.ChildFirstDownloadingClassLoader
import com.gatehill.dlcl.exclusion
import com.gatehill.dlcl.jcenter
import com.gatehill.dlcl.jitpack
import com.gatehill.dlcl.mavenCentral
import com.google.inject.Module

/**
 *
 * @author pete
 */
class PluginService {
    fun clearRepo() {
        Collector(PluginSettings.localRepo).clearCollected()
    }

    fun fetchPlugins() {
        val pluginEnvironment = loadPluginEnvironment()
        val repos = listRepos(pluginEnvironment)
        val excludes = pluginEnvironment.exclusions.map { exclusion(it.groupId, it.artifactId) }

        with(fetchPluginConfig()) {
            frontends.map { it.dependency }
                    .union(backends.map { it.dependency })
                    .union(storage.map { it.dependency })
                    .forEach { Downloader(PluginSettings.localRepo, it, excludes, repos).download() }
        }
    }

    private fun loadPluginEnvironment() = yamlMapper.readValue<PluginEnvironment>(ClassLoaderUtil.classLoader.getResourceAsStream(
            "plugin-environment.yml"), jacksonTypeRef<PluginEnvironment>())

    private fun listRepos(pluginEnvironment: PluginEnvironment = loadPluginEnvironment()) =
            listOf(mavenCentral, jcenter, jitpack).union(pluginEnvironment.repositories.toList()).toList()

    fun loadPluginInstances(): Collection<Module> {
        val classLoader = ChildFirstDownloadingClassLoader(
                PluginSettings.localRepo, listRepos(), PluginService::class.java.classLoader)

        // override the default classloader
        ClassLoaderUtil.classLoader = classLoader

        // load the already-downloaded classes
        classLoader.load()

        val pluginConfig = fetchPluginConfig()

        // frontends and backends are instantiated the same way
        return pluginConfig.frontends.union(pluginConfig.backends).flatMap { (_, classes) ->
            classes.map { className ->
                @Suppress("UNCHECKED_CAST")
                val moduleClass: Class<Module> = classLoader.loadClass(className) as Class<Module>
                moduleClass.newInstance()
            }
        }
    }

    private fun fetchPluginConfig(): PluginWrapper =
            yamlMapper.readValue(PluginSettings.pluginsFile.toFile(), PluginWrapper::class.java)
}
