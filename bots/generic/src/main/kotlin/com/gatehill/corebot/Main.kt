package com.gatehill.corebot

import com.gatehill.corebot.chat.ActionTemplateConverter
import com.gatehill.corebot.chat.NoOpActionTemplateConverter
import com.gatehill.corebot.driver.items.ItemsDriverModule
import com.gatehill.corebot.store.DataStoreModule
import com.gatehill.mcl.MavenClassLoader
import com.gatehill.mcl.jcenter
import com.gatehill.mcl.jitpack
import com.gatehill.mcl.mavenCentral
import com.google.inject.AbstractModule
import com.google.inject.Module
import org.eclipse.aether.artifact.DefaultArtifact

fun main(args: Array<String>) {
    val excludes = listOf(
            DefaultArtifact("org.jetbrains.kotlin:kotlin-stdlib:0"),
            DefaultArtifact("org.jetbrains.kotlin:kotlin-reflect:0"),
            DefaultArtifact("org.jetbrains:annotations:0"),
            DefaultArtifact("javax.inject:javax.inject:0"),
            DefaultArtifact("org.apache.logging.log4j:log4j-api:0"),
            DefaultArtifact("com.google.inject:guice:0"),
            DefaultArtifact("com.google.guava:guava:0"),
            DefaultArtifact("com.fasterxml.jackson.module:jackson-module-kotlin:0"),
            DefaultArtifact("com.fasterxml.jackson.core:jackson-databind:0"),
            DefaultArtifact("com.fasterxml.jackson.core:jackson-annotations:0"),
            DefaultArtifact("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:0"),
            DefaultArtifact("com.fasterxml.jackson.core:jackson-core:0"),
            DefaultArtifact("org.yaml:snakeyaml:0"),
            DefaultArtifact("aopalliance:aopalliance:0"),
            DefaultArtifact("com.gatehill.corebot:core-api:0"),
            DefaultArtifact("com.gatehill.corebot:core-engine:0")
    )

    val repos = listOf(
            mavenCentral,
            jcenter,
            jitpack,
            "gatehill" to "https://gatehillsoftware-maven.s3.amazonaws.com/snapshots/"
    )

    /*
    compile project(':frontends:frontends-slack')
    compile project(':backends:backends-items')
    compile project(':stores:stores-redis')
     */
    val classLoader = MavenClassLoader("target/local-repo",
            "com.gatehill.corebot:frontends-slack:0.9.0-SNAPSHOT",
            excludes,
            repos)

    @Suppress("UNCHECKED_CAST")
    val moduleClass : Class<Module> = classLoader.loadClass("com.gatehill.corebot.SlackModule") as Class<Module>
    val module = moduleClass.newInstance()

    val modules = mutableListOf<Module>(BotModule(), module)
    Bot.build(*modules.toTypedArray()).start()
}

private class BotModule : AbstractModule() {
    override fun configure() {
        bind(Bootstrap::class.java).asEagerSingleton()
        bind(ActionTemplateConverter::class.java).to(NoOpActionTemplateConverter::class.java).asSingleton()

        // data stores
        install(DataStoreModule("itemStore"))

        // drivers
        install(ItemsDriverModule())
    }
}
