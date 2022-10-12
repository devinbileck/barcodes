package com.devinbileck.barcodes.webcam;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.sarxos.webcam.WebcamException;

class NonOpeningWebcamDeviceFakeTest {
    private WebcamFake webcam;
    private final RuntimeException thrownException =
            new WebcamException("Webcam exception when opening");

    @BeforeEach
    void setup() {
        webcam = new WebcamFake(new NonOpeningWebcamDeviceFake(thrownException));
    }

    @Test
    void whenOpening_throwsException() {
        assertThrows(thrownException.getClass(), () -> webcam.open());
    }
}
