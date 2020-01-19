package com.github.fernthedev.fernapi.universal.api;

/**
 * Implement {@link ILocale} interface to replace the
 * messages to be different. You may use color codes.
 *
 * Replace in {@link com.github.fernthedev.fernapi.universal.Universal#locale}
 */
public class Locale implements ILocale {


    @Override
    public String noPermission(UniversalCommand universalCommand) {
        return "&cYou do not have permission (%permission%) to execute %command%"
                .replace("%permission", universalCommand.getPermission())
                .replace("%command%", universalCommand.getName());
    }
}
