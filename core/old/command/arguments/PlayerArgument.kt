package com.github.fernthedev.fernapi.universal.api.command.arguments

import com.github.fernthedev.fernapi.universal.Universal
import com.github.fernthedev.fernapi.universal.api.IFPlayer
import com.github.fernthedev.fernapi.universal.api.command.AbstractArgument
import com.github.fernthedev.fernapi.universal.api.command.CommandSender
import java.util.*
import java.util.stream.Collectors

open class PlayerArgument(
        identifier: String?,
        possibleValues: Set<IFPlayer<*>>,
        suggester: PlayerSuggester?,
        validator: PlayerValidator?,
        permission: String? = null
) : AbstractArgument<IFPlayer<*>> (
        identifier,
        possibleValues,
        suggester,
        validator,
        permission
) {

    override var suggester: PlayerSuggester? = PlayerSuggester();
    override var validator: PlayerValidator? = PlayerValidator();

    inner class PlayerValidator : ArgumentValidator<PlayerArgument> {
        override fun isValid(sender: CommandSender, arg: String, permission: String?, argumentHandler: PlayerArgument): ValidatorData {
            if (permission != null && !sender.hasPermission(permission)) return ValidatorData(Universal.getLocale().noPermission(arg))

            return if (Objects.requireNonNull(argumentHandler.possibleValues)!!
                            .parallelStream()
                            .anyMatch { e -> arg.equals(e.name, ignoreCase = true) })
                ValidatorData(null)
            else
                ValidatorData(Universal.getLocale().argumentNotFound(arg))
        }
    }

    inner class PlayerSuggester : ArgumentSuggester<PlayerArgument> {
        override fun getSuggestions(sender: CommandSender, arg: String, permission: String?, argumentHandler: PlayerArgument): List<String?>? {
            if (permission != null && !sender.hasPermission(permission)) emptyList<String>();
            return argumentHandler.possibleValues!!.parallelStream()
                    .filter { e -> arg.startsWith(e.name) || arg.contains(e.name) }
                    .map { obj -> obj.name }
                    .collect(Collectors.toList())
        }
    }

    override fun getValueParsed(sender: CommandSender, rawValue: String): IFPlayer<*> {
        if (permission != null && !sender.hasPermission(permission)) noPermission(rawValue);

        if (validator != null) {
            val data = validator!!.isValid(sender, rawValue, permission, this);

            if (!data.isValid()) argumentError(rawValue, data);
        }

        return possibleValues!!.parallelStream().filter { p -> p.name.equals(rawValue, ignoreCase = true) }.findFirst().get();
    }

}