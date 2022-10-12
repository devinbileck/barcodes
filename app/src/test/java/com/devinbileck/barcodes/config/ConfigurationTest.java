package com.devinbileck.barcodes.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationTest {
    @BeforeEach
    void setup() {
        System.clearProperty("simulation.enabled");
        System.clearProperty("simulation.fakeWebcams");
    }

    @Test
    void isSimulationEnabledWithPropertyNotSet_returnsFalse() {
        assertFalse(Configuration.isSimulationEnabled());
    }

    @Test
    void isSimulationEnabledWithPropertySet_returnsPropertyValue() {
        System.setProperty("simulation.enabled", "true");
        assertTrue(Configuration.isSimulationEnabled());
    }

    @Test
    void isSimulationFakeWebcamsWithPropertyNotSet_returnsFalse() {
        assertFalse(Configuration.isSimulationFakeWebcams());
    }

    @Test
    void isSimulationFakeWebcamsWithPropertySet_returnsPropertyValue() {
        System.setProperty("simulation.fakeWebcams", "true");
        assertTrue(Configuration.isSimulationFakeWebcams());
    }
}
