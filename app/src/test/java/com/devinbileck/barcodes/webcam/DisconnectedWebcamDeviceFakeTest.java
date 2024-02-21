package com.devinbileck.barcodes.webcam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DisconnectedWebcamDeviceFakeTest {
    private static final long GET_IMAGE_DELAY_MILLIS = 1_000L;

    private WebcamFake webcam;

    @BeforeEach
    void setup() {
        webcam = new WebcamFake(new DisconnectedWebcamDeviceFake(GET_IMAGE_DELAY_MILLIS));
    }

    @Test
    void whenGettingImage_initiallyReturnsImageThenReturnsNull() throws InterruptedException {
        assertThat(webcam.getImage()).isNotNull();
        Thread.sleep(GET_IMAGE_DELAY_MILLIS + 1);
        assertNull(webcam.getImage());
    }
}
