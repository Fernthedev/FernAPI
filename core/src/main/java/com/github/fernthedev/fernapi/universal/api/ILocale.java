package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.Universal;

/**
 * Implement this interface to replace the
 * messages to be different. You may use color codes.
 *
 * Replace in {@link Universal#setLocale(ILocale)}
 */
public interface ILocale {

    String noPermission(String permission, UniversalCommand command);

    String noPermission(UniversalCommand command);

}
