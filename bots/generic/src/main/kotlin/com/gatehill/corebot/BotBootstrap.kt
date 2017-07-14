package com.gatehill.corebot

import com.gatehill.corebot.action.factory.ShowHelpFactory
import com.gatehill.corebot.chat.template.TemplateService
import javax.inject.Inject

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
class BotBootstrap @Inject constructor(templateService: TemplateService) {
    init {
        templateService.registerFactory(ShowHelpFactory::class.java)
    }
}
