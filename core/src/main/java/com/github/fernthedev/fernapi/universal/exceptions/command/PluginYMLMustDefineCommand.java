package com.github.fernthedev.fernapi.universal.exceptions.command;

import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;

public class PluginYMLMustDefineCommand extends FernRuntimeException {
    public PluginYMLMustDefineCommand() {
        super();
    }

    public PluginYMLMustDefineCommand(String s) {
        super(s);
    }

    public PluginYMLMustDefineCommand(String s, Exception e) {
        super(s, e);
    }
}
