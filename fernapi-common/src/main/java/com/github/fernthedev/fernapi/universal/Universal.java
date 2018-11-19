package com.github.fernthedev.fernapi.universal;

public class Universal {

    private static Universal instance = null;
    private static MethodInterface mi;

    public static Universal getInstance() {
        return instance == null ? instance = new Universal() : instance;
    }

    public void setup(MethodInterface methodInterface) {
        mi = methodInterface;
    }

    public static MethodInterface getMethods() {
        return mi;
    }



}
