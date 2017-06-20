package com.gatehill.corebot

import com.gatehill.corebot.chat.TemplateService
import com.gatehill.corebot.chat.model.template.ShowHelpTemplate
import javax.inject.Inject

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
class BotBootstrap @Inject constructor(templateService: TemplateService) {
    init {
        templateService.registerTemplate(ShowHelpTemplate::class.java)
    }
}
