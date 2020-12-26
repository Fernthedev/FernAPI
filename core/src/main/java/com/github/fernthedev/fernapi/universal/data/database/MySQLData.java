package com.github.fernthedev.fernapi.universal.data.database;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MySQLData {

    private static final Map<Class<?>, Function<String, ?>> DECODERS = new HashMap<>();
    private static final Map<Class<?>, Function<?, String>> ENCODERS = new HashMap<>();


    public static <T> void registerEncoder(Class<T> clazz, Function<T, String> encoder) {
        ENCODERS.put(clazz, encoder);
    }

    public static <T> void registerDecoder(Class<T> clazz, Function<String, T> decoder) {
        DECODERS.put(clazz, decoder);
    }

    public static boolean hasEncoder(Class<?> clazz) {
        return ENCODERS.containsKey(clazz);
    }

    public static boolean hasDecoder(Class<?> clazz) {
        return DECODERS.containsKey(clazz);
    }

    public static <T> T decode(Class<? extends T> clazz, String value) {
        return (T) DECODERS.get(clazz).apply(value);
    }

    public static <T> String encode(Class<? extends T> clazz, T value) {
        Function<T, String> stringFunction = (Function<T, String>) ENCODERS.get(clazz);

        return stringFunction.apply(value);
    }
}
