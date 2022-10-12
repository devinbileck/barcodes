package com.devinbileck.barcodes.webcam;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.sarxos.webcam.WebcamResolution;

class WebcamDeviceFakeTest {
    private WebcamDeviceFake webcamDevice;
    private WebcamFake webcam;
    private BufferedImage img;

    @BeforeEach
    void setup() {
        img = new BufferedImage(640, 480, TYPE_INT_RGB);
        webcamDevice = new WebcamDeviceFake(img);
        webcam = new WebcamFake(webcamDevice);
    }

    @Test
    void whenInstantiatingWithCustomName_thenNameIsSet() {
        String deviceName = "custom name";
        webcamDevice = new WebcamDeviceFake(deviceName, img, 0);

        assertThat(webcamDevice.getName()).isEqualTo(deviceName);
    }

    @Test
    void whenInstantiatingWithValidImage_thenAppropriateDefaultsAreConfigured() {
        Dimension imageResolution = new Dimension(img.getWidth(), img.getHeight());

        assertThat(webcamDevice.getName()).isEqualTo("FunctioningWebcamDeviceFake");
        assertThat(webcamDevice.getResolutions()).isEqualTo(new Dimension[] {imageResolution});
        assertThat(webcamDevice.getResolution()).isEqualTo(imageResolution);
    }

    @Test
    void whenInstantiatingWithNullImage_thenAppropriateDefaultsAreConfigured() {
        WebcamDeviceFake webcamDevice = new WebcamDeviceFake(null);
        webcam = new WebcamFake(webcamDevice);

        assertThat(webcamDevice.getName()).isEqualTo("FunctioningWebcamDeviceFake");
        assertThat(webcamDevice.getResolutions())
                .isEqualTo(
                        new Dimension[] {
                            WebcamResolution.QVGA.getSize(),
                            WebcamResolution.VGA.getSize(),
                            WebcamResolution.XGA.getSize(),
                            WebcamResolution.SXGA.getSize()
                        });
        assertThat(webcamDevice.getResolution()).isEqualTo(WebcamResolution.SXGA.getSize());
    }

    @Test
    void whenSettingResolution_thenResolutionIsSet() {
        webcamDevice.setResolution(WebcamResolution.VGA.getSize());

        assertThat(webcamDevice.getResolution()).isEqualTo(WebcamResolution.VGA.getSize());
    }

    @Test
    void whenGettingImageWithNullImage_returnsNullAfterDelay() throws InterruptedException {
        WebcamDeviceFake webcamDevice = new WebcamDeviceFake(null);
        webcam = new WebcamFake(webcamDevice);

        assertThat(webcam.getImage()).isNotEqualTo(img);
        // TODO can this fixed sleep be replaced with a conditional wait?
        Thread.sleep(10000);
        assertNull(webcam.getImage());
    }

    @Test
    void whenGettingImageWithValidImage_returnsFinalImageAfterDelay() throws InterruptedException {
        assertThat(webcam.getImage()).isNotEqualTo(img);
        // TODO can this fixed sleep be replaced with a conditional wait?
        Thread.sleep(10000);
        assertThat(webcam.getImage()).isEqualTo(img);
    }

    @Test
    void whenOpening_thenIsOpened() {
        webcamDevice.open();

        assertTrue(webcamDevice.isOpen());
    }

    @Test
    void whenClosing_thenIsNotOpened() {
        webcamDevice.close();

        assertFalse(webcamDevice.isOpen());
    }
}
