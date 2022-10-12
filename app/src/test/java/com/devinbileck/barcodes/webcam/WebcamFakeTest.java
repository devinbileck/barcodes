package com.devinbileck.barcodes.webcam;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.sarxos.webcam.WebcamDevice;

@ExtendWith(MockitoExtension.class)
class WebcamFakeTest {
    private WebcamFake webcam;
    @Spy private WebcamDevice webcamDeviceSpy;

    @BeforeEach
    void setup() {
        when(webcamDeviceSpy.getName()).thenReturn("WebcamDeviceSpy");
        webcam = new WebcamFake(webcamDeviceSpy);
    }

    @Test
    void whenOpeningWebcam_thenWebcamDeviceIsOpened() {
        webcam.open();

        verify(webcamDeviceSpy).open();
    }

    @Test
    void whenClosingWebcam_thenWebcamDeviceIsClosed() {
        webcam.close();

        verify(webcamDeviceSpy).close();
    }

    @Test
    void whenDeterminingIsOpen_thenWebcamDeviceIsQueried() {
        webcam.isOpen();

        verify(webcamDeviceSpy).isOpen();
    }

    @Test
    void whenCheckingIsImageNew_thenReturnsTrueOnceFrameRateHasElapsed()
            throws InterruptedException {
        assertFalse(webcam.isImageNew());
        Thread.sleep((long) ((1 / webcam.getFPS()) * 1000) + 1L);
        assertTrue(webcam.isImageNew());
    }

    @Test
    void whenGettingImage_thenAnImageIsReturned() {
        BufferedImage img = new BufferedImage(640, 480, TYPE_INT_RGB);
        when(webcamDeviceSpy.getImage()).thenReturn(img);

        BufferedImage retrievedImage = webcam.getImage();

        assertThat(retrievedImage).isInstanceOf(BufferedImage.class);
    }
}
