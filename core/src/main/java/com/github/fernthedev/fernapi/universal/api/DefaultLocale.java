package com.github.fernthedev.fernapi.universal.api;

/**
 * Implement {@link ILocale} interface to replace the
 * messages to be different. You may use color codes.
 *
 * Replace in Universal#setLocale(ILocale)
 */
public class DefaultLocale implements ILocale {


    @Override
    public String noPermission(String permission, UniversalCommand command) {
        return "&cYou do not have permission (%permission%) to execute %command%"
                .replace("%permission%", permission)
                .replace("%command%", command.getName());
    }

    @Override
    public String noPermission(UniversalCommand universalCommand) {
        return noPermission(universalCommand.getPermission(), universalCommand);
    }
}
