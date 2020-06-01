package com.github.fernthedev.fernapi.universal.api.command.arguments

import com.github.fernthedev.fernapi.universal.Universal
import com.github.fernthedev.fernapi.universal.api.command.AbstractArgument
import com.github.fernthedev.fernapi.universal.api.command.CommandSender
import java.text.NumberFormat
import java.util.*
import java.util.stream.Collectors

open class NumArgument<T: Number>(
        identifier: String?,
        possibleValues: Set<T>?,
        suggester: ArgumentSuggester<NumArgument<T>>,
        validator: ArgumentValidator<NumArgument<T>>,
        permission: String? = null
) : AbstractArgument<T>(
        identifier,
        possibleValues,
        suggester,
        validator,
        permission
) {

    override var suggester: NumSuggester? = NumSuggester();
    override var validator: NumValidator? = NumValidator();

    open inner class NumValidator : ArgumentValidator<NumArgument<T>> {
        override fun isValid(sender: CommandSender, arg: String, permission: String?, argumentHandler: NumArgument<T>): ValidatorData {
            if (permission != null && !sender.hasPermission(permission)) return ValidatorData(Universal.getLocale().noPermission(permission))

            try {
                NumberFormat.getInstance().parse(arg)
            } catch (e: Exception) {
                return ValidatorData(Universal.getLocale().invalidNumber(arg, e.localizedMessage))
            }

            return if(arg.matches(Regex("[0-9]+")) && Objects.requireNonNull(argumentHandler.possibleValues)!!
                    .parallelStream()
                    .anyMatch { e -> arg.equals(e.toString(), ignoreCase = true) })
                ValidatorData(null)
            else
                ValidatorData(Universal.getLocale().argumentNotFound(arg))
        }
    }

    inner class NumSuggester : ArgumentSuggester<NumArgument<T>> {
        override fun getSuggestions(sender: CommandSender, arg: String, permission: String?, argumentHandler: NumArgument<T>): List<String?>? {
            if (arg.matches(Regex("[0-9]+")) || (permission != null && !sender.hasPermission(permission))) emptyList<String>();
            return argumentHandler.possibleValues!!.parallelStream()
                    .filter { e -> arg.startsWith(e.toString()) || arg.contains(e.toString()) }
                    .map { obj -> obj.toString() }
                    .collect(Collectors.toList())
        }
    }

    override fun getValueParsed(sender: CommandSender, rawValue: String): T {
        if (permission != null && !sender.hasPermission(permission)) noPermission(rawValue);

        if (validator != null) {
            val data = validator!!.isValid(sender, rawValue, permission, this);

            if (!data.isValid()) argumentError(rawValue, data);
        }

        return (NumberFormat.getInstance().parse(rawValue) as T?)!!
    }


}