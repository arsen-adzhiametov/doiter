package com.lutshe.doiter.views.util;

/**
 * Created by Arturro on 22.09.13.
 */
public final class Randomdom {
    public static <T> T choose(T... options) {
        return options[((int) (Math.random() * options.length))];
    }
}
