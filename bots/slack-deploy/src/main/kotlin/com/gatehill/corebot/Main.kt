package com.gatehill.corebot

import com.gatehill.corebot.action.ActionFactoryConverter
import com.gatehill.corebot.driver.jenkins.JenkinsDriverModule
import com.gatehill.corebot.driver.jobs.JobsDriverModule
import com.gatehill.corebot.driver.jobs.action.TriggerActionFactoryConverter
import com.gatehill.corebot.driver.rundeck.RundeckDriverModule
import com.google.inject.AbstractModule

fun main(args: Array<String>) {
    Bot.build(BotModule(), SlackModule()).start()
}

private class BotModule : AbstractModule() {
    override fun configure() {
        bind(BotBootstrap::class.java).asEagerSingleton()
        bind(ActionFactoryConverter::class.java).to(TriggerActionFactoryConverter::class.java).asSingleton()

        // drivers
        install(JobsDriverModule())
        install(JenkinsDriverModule())
        install(RundeckDriverModule())
    }
}
