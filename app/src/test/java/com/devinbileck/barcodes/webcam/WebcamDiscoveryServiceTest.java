package com.devinbileck.barcodes.webcam;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.devinbileck.barcodes.test.fakes.WebcamDriverFake;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.sarxos.webcam.Webcam;

class WebcamDiscoveryServiceTest {
    private final WebcamDiscoveryService webcamDiscoveryService = new WebcamDiscoveryService();

    @BeforeAll
    static void setDriver() {
        Webcam.setDriver(new WebcamDriverFake());
    }

    @Test
    void whenGettingWebcamsInSimulationModeWithFakeWebcams_thenFakeWebcamsReturned() {
        System.setProperty("simulation.enabled", "true");
        System.setProperty("simulation.fakeWebcams", "true");
        List<Webcam> webcams = webcamDiscoveryService.getWebcams();
        assertThat(webcams).hasSizeGreaterThan(0);
        for (Webcam webcam : webcams) {
            assertThat(webcam.getDevice().getName()).contains("WebcamDeviceFake");
        }
    }

    @Test
    void whenGettingWebcamsInSimulationModeWithoutFakeWebcams_thenNoWebcamsReturned() {
        System.setProperty("simulation.enabled", "true");
        System.setProperty("simulation.fakeWebcams", "false");
        List<Webcam> webcams = webcamDiscoveryService.getWebcams();
        assertThat(webcams).isEmpty();
    }

    @Test
    void whenGettingWebcamsNotInSimulationMode_thenActualWebcamsReturned() {
        System.setProperty("simulation.enabled", "false");
        for (Webcam webcam : webcamDiscoveryService.getWebcams()) {
            assertThat(webcam.getDevice().getName()).doesNotContain("WebcamDeviceFake");
        }
    }
}
