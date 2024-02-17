package com.devinbileck.barcodes.config;

import javax.validation.constraints.NotNull;

public class Configuration {
    private Configuration() {
        // this class cannot be instantiated
    }

    public static boolean isSimulationEnabled() {
        return isBooleanConfigOptionTrue("simulation.enabled");
    }

    public static boolean isSimulationFakeWebcams() {
        return isBooleanConfigOptionTrue("simulation.fakeWebcams");
    }

    private static boolean isBooleanConfigOptionTrue(@NotNull final String name) {
        String value = getSystemPropertyOrEnvironmentVariable(name);
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    private static String getSystemPropertyOrEnvironmentVariable(@NotNull final String name) {
        String value = System.getProperty(name);
        if (value == null) {
            value = System.getenv(name);
        }
        return value;
    }
}
