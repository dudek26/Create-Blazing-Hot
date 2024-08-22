package com.dudko.blazinghot.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;

public enum Platform {
    FORGE, FABRIC;

    @ExpectPlatform
    public static Platform getPlatform() {
        throw new AssertionError();
    }

    public boolean isCurrent() {
        return this == getPlatform();
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public String asResource(String path) {
        return this + ":" + path;
    }
}