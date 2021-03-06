package com.gatehill.corebot.driver.jobs.action.factory

import com.gatehill.corebot.action.factory.ActionMessageMode
import com.gatehill.corebot.action.factory.NamedActionFactory
import com.gatehill.corebot.action.factory.Template
import com.gatehill.corebot.action.model.ActionType
import com.gatehill.corebot.config.ConfigService
import javax.inject.Inject

/**
 * Enables a job.
 */
@Template("enableJob", builtIn = true, showInUsage = true, actionMessageMode = ActionMessageMode.INDIVIDUAL)
class EnableJobFactory @Inject constructor(configService: ConfigService) : NamedActionFactory(configService) {
    override val actionType: ActionType = JobActionType.ENABLE
}
