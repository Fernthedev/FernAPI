package com.github.fernthedev.fernapi.universal.exceptions.network;

import com.github.fernthedev.fernapi.universal.exceptions.FernException;
import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;

import java.net.SocketTimeoutException;

public class PluginTimeoutException extends FernRuntimeException {
    public PluginTimeoutException() {
        super();
    }

    public PluginTimeoutException(String s) {
        super(s);
    }

    public PluginTimeoutException(String s, Exception e) {
        super(s, e);
    }
}
