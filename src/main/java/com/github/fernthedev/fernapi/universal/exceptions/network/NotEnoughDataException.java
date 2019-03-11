package com.github.fernthedev.fernapi.universal.exceptions.network;

import java.io.IOException;

public class NotEnoughDataException extends IOException {

    public NotEnoughDataException(String s) {
        super(s);
    }

    public NotEnoughDataException(String s, IOException e) {
        super(s,e);
    }
}
