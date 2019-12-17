package com.github.fernthedev.fernapi.universal.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FernException extends Exception implements FernThrowable {

    public FernException(String s) {
        super(s);
    }

    public FernException(String s,Exception e) {
        super(s,e);
    }

}
