package com.lutshe.doiter.preloaders;

/**
 * Created by Arturro on 26.08.13.
 */
public interface Loader {
    long getLoadingInterval();
    void load();
}
