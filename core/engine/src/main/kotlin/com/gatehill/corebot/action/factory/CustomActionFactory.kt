package com.gatehill.corebot.action.factory

import com.gatehill.corebot.action.model.Action
import com.gatehill.corebot.action.model.CustomAction
import com.gatehill.corebot.action.model.TriggerContext
import com.gatehill.corebot.config.model.ActionConfig
import com.gatehill.corebot.config.model.TransformType
import java.util.HashMap

/**
 * Represents a custom action.
 */
abstract class CustomActionFactory : BaseActionFactory() {
    /**
     * List the actions from this template.
     */
    override fun buildActions(trigger: TriggerContext): List<Action> {
        return actionConfigs.map { actionConfig ->
            val options = transform(actionConfig, placeholderValues)
            CustomAction(actionType,
                    buildShortDescription(actionConfig),
                    if (actionMessageMode == ActionMessageMode.INDIVIDUAL && actionConfig.showJobOutcome) buildStartMessage(trigger, options, actionConfig) else null,
                    actionConfig.tags,
                    actionConfig.driver,
                    actionConfig,
                    options)
        }
    }

    private fun transform(actionConfig: ActionConfig, options: MutableMap<String, String>): Map<String, String> {
        val transformed: MutableMap<String, String> = HashMap(options)

        actionConfig.options.map { Pair(it.key, it.value.transformers) }.forEach { optionTransform ->
            val optionKey = optionTransform.first

            var optionValue = options[optionKey]
            optionValue?.let {
                optionTransform.second.forEach { transformType ->
                    optionValue = when (transformType) {
                        TransformType.LOWERCASE -> optionValue!!.toLowerCase()
                        TransformType.UPPERCASE -> optionValue!!.toUpperCase()
                        else -> throw UnsupportedOperationException("Transform type ${transformType} is not supported")
                    }
                }
                transformed[optionKey] = optionValue!!
            }
        }

        return transformed
    }
}
