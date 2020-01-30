package com.github.fernthedev.fernapi.universal.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FernDebugException extends FernRuntimeException {
    public FernDebugException(String s) {
        super(s);
    }

    public FernDebugException(String s, Exception e) {
        super(s,e);
    }
}
