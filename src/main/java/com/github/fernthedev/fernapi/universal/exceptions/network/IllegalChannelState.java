package com.github.fernthedev.fernapi.universal.exceptions.network;

import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;

public class IllegalChannelState extends FernRuntimeException {
    public IllegalChannelState() {
        super();
    }

    public IllegalChannelState(String s) {
        super(s);
    }

    public IllegalChannelState(String s, Exception e) {
        super(s, e);
    }
}
