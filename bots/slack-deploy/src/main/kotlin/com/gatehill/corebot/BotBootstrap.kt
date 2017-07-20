package com.gatehill.corebot

import com.gatehill.corebot.action.factory.ShowHelpFactory
import com.gatehill.corebot.chat.template.TemplateService
import com.gatehill.corebot.driver.ActionDriverFactory
import com.gatehill.corebot.driver.jenkins.action.JenkinsActionDriver
import com.gatehill.corebot.driver.rundeck.action.RundeckActionDriver
import javax.inject.Inject

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
class BotBootstrap @Inject constructor(actionDriverFactory: ActionDriverFactory,
                                       templateService: TemplateService) {
    init {
        // drivers
        actionDriverFactory.registerDriver("rundeck", RundeckActionDriver::class.java)
        actionDriverFactory.registerDriver("jenkins", JenkinsActionDriver::class.java)

        // templates
        templateService.registerFactory(ShowHelpFactory::class.java)
    }
}
