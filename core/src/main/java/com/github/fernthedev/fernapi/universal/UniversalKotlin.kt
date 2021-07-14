package com.github.fernthedev.fernapi.universal

import java.util.*

/**
 * Lazy debug logger to improve performance
 */
fun debugLog(supplier: () -> String) {
    if (Universal.isDebug()) {
        Universal.debug(supplier())
    }
}

/**
 * Lazy debug logger to improve performance
 */
@JvmName("debugLog1")
fun debugLog(supplier: () -> String, objects: () -> Array<Any>) {
    if (Universal.isDebug()) {
        Universal.debug(supplier(), objects())
    }
}