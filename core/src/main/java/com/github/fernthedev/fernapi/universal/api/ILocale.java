package com.github.fernthedev.fernapi.universal.api;

/**
 * Implement this interface to replace the
 * messages to be different. You may use color codes.
 *
 * Replace in Universal#setLocale(ILocale)
 */
public interface ILocale {

    String noPermission(String permission, UniversalCommand command);

    String noPermission(UniversalCommand command);

}
