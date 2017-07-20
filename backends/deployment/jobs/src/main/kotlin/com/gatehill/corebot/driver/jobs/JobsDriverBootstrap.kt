package com.gatehill.corebot.driver.jobs

import com.gatehill.corebot.action.factory.LockActionFactory
import com.gatehill.corebot.action.factory.LockOptionFactory
import com.gatehill.corebot.action.factory.StatusActionFactory
import com.gatehill.corebot.action.factory.UnlockActionFactory
import com.gatehill.corebot.action.factory.UnlockOptionFactory
import com.gatehill.corebot.chat.template.TemplateConfigService
import com.gatehill.corebot.chat.template.TemplateService
import com.gatehill.corebot.driver.jobs.action.factory.DisableJobFactory
import com.gatehill.corebot.driver.jobs.action.factory.EnableJobFactory
import javax.inject.Inject

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
class JobsDriverBootstrap @Inject constructor(templateService: TemplateService,
                                              templateConfigService: TemplateConfigService) {
    init {
        templateConfigService.registerClasspathTemplateFile("/jobs-templates.yml")
        templateService.registerFactory(LockActionFactory::class.java)
        templateService.registerFactory(UnlockActionFactory::class.java)
        templateService.registerFactory(StatusActionFactory::class.java)
        templateService.registerFactory(EnableJobFactory::class.java)
        templateService.registerFactory(DisableJobFactory::class.java)
        templateService.registerFactory(LockOptionFactory::class.java)
        templateService.registerFactory(UnlockOptionFactory::class.java)
    }
}
