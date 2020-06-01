package com.github.fernthedev.fernapi.universal.api.command.arguments

import com.github.fernthedev.fernapi.universal.Universal
import com.github.fernthedev.fernapi.universal.api.command.AbstractArgument
import com.github.fernthedev.fernapi.universal.api.command.CommandSender
import java.util.*
import kotlin.collections.HashSet

open class CategoryArgument(
        protected val argumentMap: Map<String, AbstractArgument<*>>,
        identifier: String?,
        suggester: ParameterSuggester? = null,
        validator: ParameterValidator? = null,
        permission: String? = null
) : AbstractArgument<AbstractArgument<*>>(
        identifier = identifier,
        possibleValues = HashSet(argumentMap.values),
        suggester = suggester,
        validator = validator,
        permission = permission
) {

    override val isMultiArgument: Boolean = true

    override val countOfArgs: Int
        get() {
            var newCount: Int = super.countOfArgs;

            for (arg in argumentMap.values)
                newCount += arg.countOfArgs

            return newCount
        }

    override var suggester: ParameterSuggester? = ParameterSuggester();
    override var validator: ParameterValidator? = ParameterValidator();

    inner class ParameterValidator : ArgumentValidator<CategoryArgument> {
        override fun isValid(sender: CommandSender, arg: String, permission: String?, argumentHandler: CategoryArgument): ValidatorData {
            if (permission != null && !sender.hasPermission(permission)) return ValidatorData(Universal.getLocale().noPermission(permission))

            return if (!Objects.requireNonNull(argumentHandler.argumentMap)
                    .any { e -> arg.equals(e.key, ignoreCase = true) })
                ValidatorData(Universal.getLocale().argumentNotFound(arg))
            else
                ValidatorData(null)
        }
    }

    inner class ParameterSuggester : ArgumentSuggester<CategoryArgument> {
        override fun getSuggestions(sender: CommandSender, arg: String, permission: String?, argumentHandler: CategoryArgument): List<String?>? {
            if (permission != null && !sender.hasPermission(permission)) emptyList<String>();
            return argumentHandler.argumentMap
                    .filter { e -> arg.startsWith(e.key) || arg.contains(e.key) }
                    .map { obj -> obj.key }

                    // .collect(Collectors.toList())
        }
    }

    override fun getValueParsed(sender: CommandSender, rawValue: String): AbstractArgument<*> {
        if (permission != null && !sender.hasPermission(permission)) noPermission(rawValue);

        if (validator != null) {
            val data = validator!!.isValid(sender, rawValue, permission, this);

            if (!data.isValid()) argumentError(rawValue, data);
        }

        return argumentMap.getValue(rawValue)
    }


}