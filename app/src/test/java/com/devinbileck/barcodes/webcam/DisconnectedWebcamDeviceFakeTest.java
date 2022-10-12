package com.devinbileck.barcodes.webcam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisconnectedWebcamDeviceFakeTest {
    private WebcamFake webcam;

    @BeforeEach
    void setup() {
        webcam = new WebcamFake(new DisconnectedWebcamDeviceFake());
    }

    @Test
    void whenGettingImage_initiallyReturnsImageThenReturnsNull() throws InterruptedException {
        assertThat(webcam.getImage()).isNotNull();
        // TODO can this fixed sleep be replaced with a conditional wait?
        Thread.sleep(10000);
        assertNull(webcam.getImage());
    }
}
