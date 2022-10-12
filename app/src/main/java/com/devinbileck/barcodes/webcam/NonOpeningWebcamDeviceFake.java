package com.devinbileck.barcodes.webcam;

public class NonOpeningWebcamDeviceFake extends WebcamDeviceFake {
    private final RuntimeException thrownException;

    public NonOpeningWebcamDeviceFake(RuntimeException thrownException) {
        super("NonOpeningWebcamDeviceFake", null, 0);
        this.thrownException = thrownException;
    }

    @Override
    public void open() {
        throw thrownException;
    }
}
