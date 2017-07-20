package com.gatehill.corebot.driver.items

import com.gatehill.corebot.chat.template.TemplateConfigService
import com.gatehill.corebot.chat.template.TemplateService
import com.gatehill.corebot.driver.ActionDriverFactory
import com.gatehill.corebot.driver.items.action.ItemsActionDriverImpl
import com.gatehill.corebot.driver.items.action.factory.BorrowItemAsUserFactory
import com.gatehill.corebot.driver.items.action.factory.BorrowItemFactory
import com.gatehill.corebot.driver.items.action.factory.EvictItemFactory
import com.gatehill.corebot.driver.items.action.factory.EvictUserFromItemFactory
import com.gatehill.corebot.driver.items.action.factory.ReturnItemFactory
import com.gatehill.corebot.driver.items.action.factory.StatusAllFactory
import com.gatehill.corebot.driver.items.action.factory.StatusItemFactory
import javax.inject.Inject

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
class ItemsDriverBootstrap @Inject constructor(actionDriverFactory: ActionDriverFactory,
                                               templateService: TemplateService,
                                               templateConfigService: TemplateConfigService) {
    init {
        // drivers
        actionDriverFactory.registerDriver("items", ItemsActionDriverImpl::class.java)

        // templates
        templateConfigService.registerClasspathTemplateFile("/items-templates.yml")
        templateService.registerFactory(BorrowItemFactory::class.java)
        templateService.registerFactory(BorrowItemAsUserFactory::class.java)
        templateService.registerFactory(ReturnItemFactory::class.java)
        templateService.registerFactory(EvictItemFactory::class.java)
        templateService.registerFactory(EvictUserFromItemFactory::class.java)
        templateService.registerFactory(StatusItemFactory::class.java)
        templateService.registerFactory(StatusAllFactory::class.java)
    }
}
