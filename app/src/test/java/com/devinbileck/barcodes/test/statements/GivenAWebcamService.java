package com.devinbileck.barcodes.test.statements;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;

import com.devinbileck.barcodes.webcam.DisconnectedWebcamDeviceFake;
import com.devinbileck.barcodes.webcam.NonOpeningWebcamDeviceFake;
import com.devinbileck.barcodes.webcam.WebcamDeviceFake;
import com.devinbileck.barcodes.webcam.WebcamFake;
import com.devinbileck.barcodes.webcam.WebcamService;

public class GivenAWebcamService {
    private final WebcamService webcamService;

    public GivenAWebcamService(final WebcamService webcamService) {
        this.webcamService = webcamService;
    }

    @SuppressWarnings("EmptyMethod")
    public void withNoWebcam() {
        // Nothing to do!
    }

    public void withNonOpeningWebcamDevice(final RuntimeException thrownException) {
        WebcamDevice webcamDevice = new NonOpeningWebcamDeviceFake(thrownException);
        Webcam webcam = new WebcamFake(webcamDevice);
        webcamService.setWebcam(webcam);
    }

    public void withDisconnectedWebcamDevice() {
        WebcamDevice webcamDevice = new DisconnectedWebcamDeviceFake();
        Webcam webcam = new WebcamFake(webcamDevice);
        webcamService.setWebcam(webcam);
    }

    public void withFunctioningWebcamDevice(final BufferedImage bufferedImage) {
        WebcamDevice webcamDevice = new WebcamDeviceFake(bufferedImage);
        Webcam webcam = new WebcamFake(webcamDevice);
        webcamService.setWebcam(webcam);
    }
}
