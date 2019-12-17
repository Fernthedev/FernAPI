package com.github.fernthedev.fernapi.universal.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FernRuntimeException extends RuntimeException implements FernThrowable {
    public FernRuntimeException(String s) {
        super(s);
    }

    public FernRuntimeException(String s, Exception e) {
        super(s,e);
    }
}
