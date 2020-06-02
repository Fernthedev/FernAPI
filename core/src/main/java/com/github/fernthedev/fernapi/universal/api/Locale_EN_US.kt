package com.github.fernthedev.fernapi.universal.api

import co.aikar.commands.BaseCommand
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor

/**
 * Implement [ILocale] interface to replace the
 * messages to be different. You may use color codes.
 *
 * Replace in Universal#setLocale(ILocale)
 */
open class Locale_EN_US : ILocale {
    override fun noPermission(permission: Set<String>, command: BaseCommand): String {
        return "${ChatColor.RED}You do not have permission ($permission) to execute $command"
    }

    override fun noPermission(permission: String): String {
        return "${ChatColor.RED}You do not have permission (${permission}) to execute this"
    }

    override fun noPermission(universalCommand: BaseCommand): String {
        return noPermission(universalCommand.requiredPermissions, universalCommand)
    }

    override fun boolColored(value: Boolean): String {
        val color = if (value) ChatColor.GREEN
        else ChatColor.RED;

        return "$color$value"
    }

    override fun invalidNumber(arg: String, localizedMessage: String?): String {
        return "${ChatColor.RED}The number format is incorrect. Given: $arg Error: $localizedMessage"
    }

    override fun argumentNotFound(arg: String): String {
        return "${ChatColor.RED}Cannot find argument $arg"
    }
}