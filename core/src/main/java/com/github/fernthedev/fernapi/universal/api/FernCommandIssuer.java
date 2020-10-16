package com.github.fernthedev.fernapi.universal.api;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.CommandManager;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.fernthedev.fernapi.universal.api.IFPlayer.VANISH_PERMISSIONS;

public interface FernCommandIssuer extends CommandIssuer, Audience {
    /**
     * @param message
     * @deprecated Do not call this, for internal use. Not considered part of the API and may break.
     */
    @Deprecated
    @Override
    default void sendMessageInternal(String message) {
        sendMessage(TextMessage.fromColor(message));
    }

    void sendMessage(BaseMessage message);

    default boolean hasVanishPermission(String... strings) {
        List<String> vanishPermissions = new ArrayList<>(VANISH_PERMISSIONS);

        vanishPermissions.addAll(Arrays.asList(strings));

        return vanishPermissions.parallelStream().anyMatch(this::hasPermission);
    }


    @Override
    default CommandManager getManager() {
        return Universal.getCommandHandler();
    }

    Audience getAudience();

    /**
     * Sends a chat message.
     *
     * @param source  the identity of the source of the message
     * @param message a message
     * @param type    the type
     * @see Component
     * @since 4.0.0
     */
    @Override
    default void sendMessage(@NonNull Identity source, @NonNull Component message, @NonNull MessageType type) {
        getAudience().sendMessage(source, message, type);
    }


    /**
     * Sends a message on the action bar.
     *
     * @param message a message
     * @see Component
     * @since 4.0.0
     */
    @Override
    default void sendActionBar(@NonNull Component message) {
        getAudience().sendActionBar(message);
    }

    /**
     * Shows a title.
     *
     * @param title a title
     * @see Title
     * @since 4.0.0
     */
    @Override
    default void showTitle(@NonNull Title title) {
        getAudience().showTitle(title);
    }

    /**
     * Clears the title, if one is being displayed.
     *
     * @see Title
     * @since 4.0.0
     */
    @Override
    default void clearTitle() {
        getAudience().clearTitle();
    }

    /**
     * Resets the title and timings back to their default.
     *
     * @see Title
     * @since 4.0.0
     */
    @Override
    default void resetTitle() {
        getAudience().resetTitle();
    }

    /**
     * Shows a boss bar.
     *
     * @param bar a boss bar
     * @see BossBar
     * @since 4.0.0
     */
    @Override
    default void showBossBar(@NonNull BossBar bar) {
        getAudience().showBossBar(bar);
    }

    /**
     * Hides a boss bar.
     *
     * @param bar a boss bar
     * @see BossBar
     * @since 4.0.0
     */
    @Override
    default void hideBossBar(@NonNull BossBar bar) {
        getAudience().hideBossBar(bar);
    }

    /**
     * Plays a sound.
     *
     * @param sound a sound
     * @see Sound
     * @since 4.0.0
     */
    @Override
    default void playSound(@NonNull Sound sound) {
        getAudience().playSound(sound);
    }

    /**
     * Plays a sound at a location.
     *
     * @param sound a sound
     * @param x     x coordinate
     * @param y     y coordinate
     * @param z     z coordinate
     * @see Sound
     * @since 4.0.0
     */
    @Override
    default void playSound(@NonNull Sound sound, double x, double y, double z) {
        getAudience().playSound(sound, x, y, z);
    }

    /**
     * Stops a sound, or many sounds.
     *
     * @param stop a sound stop
     * @see SoundStop
     * @since 4.0.0
     */
    @Override
    default void stopSound(@NonNull SoundStop stop) {
        getAudience().stopSound(stop);
    }

    /**
     * Opens a book.
     *
     * <p>When possible, no item should persist after closing the book.</p>
     *
     * @param book a book
     * @see Book
     * @since 4.0.0
     */
    @Override
    default void openBook(@NonNull Book book) {
        getAudience().openBook(book);
    }
}
