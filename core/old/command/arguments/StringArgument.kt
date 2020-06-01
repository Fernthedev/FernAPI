package com.github.fernthedev.fernapi.universal.api.command.arguments

import com.github.fernthedev.fernapi.universal.Universal
import com.github.fernthedev.fernapi.universal.api.command.AbstractArgument
import com.github.fernthedev.fernapi.universal.api.command.CommandSender
import java.util.*
import java.util.stream.Collectors

open class StringArgument(
        identifier: String?,
        possibleValues: Set<String>?,
        suggester: StringSuggester?,
        validator: StringValidator?,
        permission: String? = null
) : AbstractArgument<String>(
        identifier,
        possibleValues,
        suggester,
        validator,
        permission
) {

    override var suggester: StringSuggester? = StringSuggester();
    override var validator: StringValidator? = StringValidator();

    inner class StringValidator : ArgumentValidator<StringArgument> {
        override fun isValid(sender: CommandSender, arg: String, permission: String?, argumentHandler: StringArgument): ValidatorData {
            if (permission != null && !sender.hasPermission(permission)) return ValidatorData(Universal.getLocale().noPermission(permission))

            return if (Objects.requireNonNull(argumentHandler.possibleValues)!!
                            .parallelStream()
                            .anyMatch { e -> arg.equals(e.toString(), ignoreCase = true) })
                ValidatorData(null)
            else
                ValidatorData(Universal.getLocale().argumentNotFound(arg))
        }
    }

    inner class StringSuggester : ArgumentSuggester<StringArgument> {
        override fun getSuggestions(sender: CommandSender, arg: String, permission: String?, argumentHandler: StringArgument): List<String?>? {
            if (permission != null && !sender.hasPermission(permission)) emptyList<String>();
            return argumentHandler.possibleValues!!.parallelStream()
                    .filter { e: String -> arg.startsWith(e) || arg.contains(e) }
                    .map { obj: String -> obj }
                    .collect(Collectors.toList())
        }
    }

    override fun getValueParsed(sender: CommandSender, rawValue: String): String {
        if (permission != null && !sender.hasPermission(permission)) noPermission(rawValue);

        if (validator != null) {
            val data = validator!!.isValid(sender, rawValue, permission, this);

            if (!data.isValid()) argumentError(rawValue, data);
        }

        return rawValue
    }

}