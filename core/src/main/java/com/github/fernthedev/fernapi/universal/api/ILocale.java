package com.github.fernthedev.fernapi.universal.api;

import co.aikar.commands.BaseCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Implement this interface to replace the
 * messages to be different. You may use color codes.
 *
 * Replace in Universal#setLocale(ILocale)
 */
public interface ILocale {

    String boolColored(boolean value);

    String noPermission(Set<String> permission, BaseCommand command);

    String noPermission(String permission);

    String noPermission(BaseCommand command);

    @NotNull
    String invalidNumber(@NotNull String arg, @Nullable String localizedMessage);

    String argumentNotFound(@NotNull String arg);
}
