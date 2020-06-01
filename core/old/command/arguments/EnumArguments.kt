package com.github.fernthedev.fernapi.universal.api.command.arguments

import com.github.fernthedev.fernapi.universal.Universal
import com.github.fernthedev.fernapi.universal.api.command.AbstractArgument
import com.github.fernthedev.fernapi.universal.api.command.CommandSender
import java.util.*
import java.util.stream.Collectors

class EnumArguments<T : Enum<T>, V : EnumSet<T>>(
        identifier: String?,
        possibleValues: V,
        suggester: ArgumentSuggester<out AbstractArgument<T>>?,
        validator: ArgumentValidator<out AbstractArgument<T>>?,
        permission: String? = null
) : AbstractArgument<T>(
        identifier,
        possibleValues,
        suggester,
        validator,
        permission
) {
    override var suggester: EnumSuggester = EnumSuggester();
    override var validator: EnumValidator = EnumValidator();

    inner class EnumValidator : ArgumentValidator<EnumArguments<T, V>> {
        override fun isValid(sender: CommandSender, arg: String, permission: String?, argumentHandler: EnumArguments<T, V>): ValidatorData {
            if (permission != null && !sender.hasPermission(permission)) return ValidatorData(Universal.getLocale().noPermission(permission))

            return if (Objects.requireNonNull(argumentHandler.possibleValues)!!
                            .parallelStream()
                            .anyMatch { e: T -> arg.equals(e.toString(), ignoreCase = true) })
                ValidatorData(null)
            else
                ValidatorData(Universal.getLocale().argumentNotFound(arg))
        }
    }

    inner class EnumSuggester : ArgumentSuggester<EnumArguments<T, V>> {
        override fun getSuggestions(sender: CommandSender, arg: String, permission: String?, argumentHandler: EnumArguments<T, V>): List<String?>? {
            if (permission != null && !sender.hasPermission(permission)) ArrayList<Any>()
            return argumentHandler.possibleValues!!.parallelStream()
                    .filter { e: T -> arg.startsWith(e.toString()) || arg.contains(e.toString()) }
                    .map { obj: T -> obj.toString() }
                    .collect(Collectors.toList())
        }
    }

    override fun getValueParsed(sender: CommandSender, rawValue: String): T {
        if (permission != null && !sender.hasPermission(permission)) noPermission(rawValue);

        val data = validator.isValid(sender, rawValue, permission, this);

        if (!data.isValid()) argumentError(rawValue, data);

        return possibleValues!!.parallelStream().filter { e -> e.toString().equals(rawValue, ignoreCase = true)}.findFirst().get()
    }
}