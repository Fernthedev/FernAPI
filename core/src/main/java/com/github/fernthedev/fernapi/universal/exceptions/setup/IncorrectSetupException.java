package com.github.fernthedev.fernapi.universal.exceptions.setup;

import com.github.fernthedev.fernapi.universal.exceptions.FernException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IncorrectSetupException extends FernException {

    public IncorrectSetupException(String s) {
        super(s);
    }

    public IncorrectSetupException(String s, Exception e) {
        super(s,e);
    }

}
