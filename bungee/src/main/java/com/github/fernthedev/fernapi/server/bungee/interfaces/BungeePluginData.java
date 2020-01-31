package com.github.fernthedev.fernapi.server.bungee.interfaces;

import com.github.fernthedev.fernapi.universal.api.PluginData;
import com.github.fernthedev.fernapi.universal.api.PluginLoadOrder;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.PluginDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class BungeePluginData implements PluginData<PluginDescription> {

    @NonNull
    private final PluginDescription bungeePluginData;


    /**
     * @return the server specific instance. Use for checking equality or getting server-specific data
     */
    @Override
    public PluginDescription getPluginDataInstance() {
        return bungeePluginData;
    }

    /**
     * Gives the name of the plugin. This name is a unique identifier for
     * plugins.
     * <ul>
     * <li>Must consist of all alphanumeric characters, underscores, hyphon,
     *     and period (a-z,A-Z,0-9, _.-). Any other character will cause the
     *     plugin.yml to fail loading.
     * <li>Used to determine the name of the plugin's data folder. Data
     *     folders are placed in the ./plugins/ directory by default, but this
     *     behavior should not be relied on. {@link MethodInterface#getDataFolder()}
     *     should be used to reference the data folder.
     * <li>It is good practice to name your jar the same as this, for example
     *     'MyPlugin.jar'.
     * <li>Case sensitive.
     * <li>The is the token referenced in {@link #getDepend()}, {@link
     *     #getSoftDepend()}, and {@link #getLoadBefore()}.
     * <li>Using spaces in the plugin's name is deprecated.
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>name</code>.
     * <p>
     * Example:<blockquote><pre>name: MyPlugin</pre></blockquote>
     *
     * @return the name of the plugin
     */
    @SneakyThrows
    @NotNull
    @Override
    public String getName() {
        return bungeePluginData.getName();
    }

    /**
     * Gives the version of the plugin.
     * <ul>
     * <li>Version is an arbitrary string, however the most common format is
     *     MajorRelease.MinorRelease.Build (eg: 1.4.1).
     * <li>Typically you will increment this every time you release a new
     *     feature or bug fix.
     * <li>Displayed when a user types <code>/version PluginName</code>
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>version</code>.
     * <p>
     * Example:<blockquote><pre>version: 1.4.1</pre></blockquote>
     *
     * @return the version of the plugin
     */
    @NotNull
    @Override
    public String getVersion() {
        return bungeePluginData.getVersion();
    }

    /**
     * Gives the fully qualified name of the main class for a plugin. The
     * format should follow the {@link ClassLoader#loadClass(String)} syntax
     * to successfully be resolved at runtime. For most plugins, this is the
     * class that extends {@link FernAPIPlugin}.
     * <ul>
     * <li>This must contain the full namespace including the class file
     *     itself.
     * <li>If your namespace is <code>org.bukkit.plugin</code>, and your class
     *     file is called <code>MyPlugin</code> then this must be
     *     <code>org.bukkit.plugin.MyPlugin</code>
     * <li>No plugin can use <code>org.bukkit.</code> as a base package for
     *     <b>any class</b>, including the main class.
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>main</code>.
     * <p>
     * Example:
     * <blockquote><pre>main: org.bukkit.plugin.MyPlugin</pre></blockquote>
     *
     * @return the fully qualified main class for the plugin
     */
    @NotNull
    @Override
    public String getMain() {
        return bungeePluginData.getMain();
    }

    /**
     * Gives a human-friendly description of the functionality the plugin
     * provides.
     * <ul>
     * <li>The description can have multiple lines.
     * <li>Displayed when a user types <code>/version PluginName</code>
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>description</code>.
     * <p>
     * Example:
     * <blockquote><pre>description: This plugin is so 31337. You can set yourself on fire.</pre></blockquote>
     *
     * @return description of this plugin, or null if not specified
     */
    @Nullable
    @Override
    public String getDescription() {
        return bungeePluginData.getDescription();
    }

    /**
     * Gives the phase of server startup that the plugin should be loaded.
     * <ul>
     * <li>Possible values are in {@link PluginLoadOrder}.
     * <li>Defaults to {@link PluginLoadOrder#POSTWORLD}.
     * <li>Certain caveats apply to each phase.
     * <li>When different, {@link #getDepend()}, {@link #getSoftDepend()}, and
     *     {@link #getLoadBefore()} become relative in order loaded per-phase.
     *     If a plugin loads at <code>STARTUP</code>, but a dependency loads
     *     at <code>POSTWORLD</code>, the dependency will not be loaded before
     *     the plugin is loaded.
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>load</code>.
     * <p>
     * Example:<blockquote><pre>load: STARTUP</pre></blockquote>
     *
     * @return the phase when the plugin should be loaded
     */
    @NotNull
    @Override
    public PluginLoadOrder getLoad() {
        return PluginLoadOrder.STARTUP;
    }

    /**
     * Gives the list of authors for the plugin.
     * <ul>
     * <li>Gives credit to the developer.
     * <li>Used in some server error messages to provide helpful feedback on
     *     who to contact when an error occurs.
     * <li>A bukkit.org forum handle or email address is recommended.
     * <li>Is displayed when a user types <code>/version PluginName</code>
     * <li><code>authors</code> must be in <a
     *     href="http://en.wikipedia.org/wiki/YAML#Lists">YAML list
     *     format</a>.
     * </ul>
     * <p>
     * In the plugin.yml, this has two entries, <code>author</code> and
     * <code>authors</code>.
     * <p>
     * Single author example:
     * <blockquote><pre>author: CaptainInflamo</pre></blockquote>
     * Multiple author example:
     * <blockquote><pre>authors: [Cogito, verrier, EvilSeph]</pre></blockquote>
     * When both are specified, author will be the first entry in the list, so
     * this example:
     * <blockquote><pre>author: Grum
     * authors:
     * - feildmaster
     * - amaranth</pre></blockquote>
     * Is equivilant to this example:
     * <pre>authors: [Grum, feildmaster, aramanth]</pre>
     *
     * @return an immutable list of the plugin's authors
     */
    @NotNull
    @Override
    public List<String> getAuthors() {
        return new ArrayList<>(Collections.singleton(bungeePluginData.getAuthor()));
    }

    /**
     * Gives the plugin's or plugin's author's website.
     * <ul>
     * <li>A link to the Curse page that includes documentation and downloads
     *     is highly recommended.
     * <li>Displayed when a user types <code>/version PluginName</code>
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>website</code>.
     * <p>
     * Example:
     * <blockquote><pre>website: http://www.curse.com/server-mods/minecraft/myplugin</pre></blockquote>
     *
     * @return description of this plugin, or null if not specified
     */
    @Nullable
    @Override
    public String getWebsite() {
        return "";
    }

    /**
     * Gives a list of other plugins that the plugin requires.
     * <ul>
     * <li>Use the value in the {@link #getName()} of the target plugin to
     *     specify the dependency.
     * <li>If any plugin listed here is not found, your plugin will fail to
     *     load at startup.
     * <li>If multiple plugins list each other in <code>depend</code>,
     *     creating a network with no individual plugin does not list another
     *     plugin in the <a
     *     href=https://en.wikipedia.org/wiki/Circular_dependency>network</a>,
     *     all plugins in that network will fail.
     * <li><code>depend</code> must be in must be in <a
     *     href="http://en.wikipedia.org/wiki/YAML#Lists">YAML list
     *     format</a>.
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>depend</code>.
     * <p>
     * Example:
     * <blockquote><pre>depend:
     * - OnePlugin
     * - AnotherPlugin</pre></blockquote>
     *
     * @return immutable list of the plugin's dependencies
     */
    @NotNull
    @Override
    public List<String> getDepend() {
        return new ArrayList<>(bungeePluginData.getDepends());
    }

    /**
     * Gives a list of other plugins that the plugin requires for full
     * functionality. The PluginManager will make best effort to treat
     * all entries here as if they were a {@link #getDepend() dependency}, but
     * will never fail because of one of these entries.
     * <ul>
     * <li>Use the value in the {@link #getName()} of the target plugin to
     *     specify the dependency.
     * <li>When an unresolvable plugin is listed, it will be ignored and does
     *     not affect load order.
     * <li>When a circular dependency occurs (a network of plugins depending
     *     or soft-dependending each other), it will arbitrarily choose a
     *     plugin that can be resolved when ignoring soft-dependencies.
     * <li><code>softdepend</code> must be in <a
     *     href="http://en.wikipedia.org/wiki/YAML#Lists">YAML list
     *     format</a>.
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>softdepend</code>.
     * <p>
     * Example:
     * <blockquote><pre>softdepend: [OnePlugin, AnotherPlugin]</pre></blockquote>
     *
     * @return immutable list of the plugin's preferred dependencies
     */
    @NotNull
    @Override
    public List<String> getSoftDepend() {
        return new ArrayList<>(bungeePluginData.getSoftDepends());
    }

    /**
     * Gives the token to prefix plugin-specific logging messages with.
     * <ul>
     * <li>This includes all messages using {@link Logger} ()}.
     * <li>If not specified, the server uses the plugin's {@link #getName()
     *     name}.
     * <li>This should clearly indicate what plugin is being logged.
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>prefix</code>.
     * <p>
     * Example:<blockquote><pre>prefix: ex-why-zee</pre></blockquote>
     *
     * @return the prefixed logging token, or null if not specified
     */
    @Nullable
    @Override
    public String getPrefix() {
        return bungeePluginData.getName();
    }
}
