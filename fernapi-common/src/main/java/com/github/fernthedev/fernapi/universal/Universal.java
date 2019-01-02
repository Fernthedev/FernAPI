package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;

public class Universal {

    private Universal() {}

    private static Universal instance = null;
    private static MethodInterface mi;

    public static Universal getInstance() {
        return instance == null ? instance = new Universal() : instance;
    }

    public void setup(MethodInterface methodInterface) {
        methodInterface.getLogger().info("Registered interface");
        mi = methodInterface;
    }

    public static MethodInterface getMethods() {
        return mi;
    }



}
