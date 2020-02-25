package com.github.fernthedev.fernapi.universal.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VersionData {

    private final String fernapi_version;

    @Override
    public String toString() {
        return fernapi_version;
    }
}
