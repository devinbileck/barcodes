package com.devinbileck.barcodes.webcam;

public class DisconnectedWebcamDeviceFake extends WebcamDeviceFake {
    public static final long GET_IMAGE_DELAY_MILLIS = 5000L;

    public DisconnectedWebcamDeviceFake() {
        super("DisconnectedWebcamDeviceFake", null, GET_IMAGE_DELAY_MILLIS);
    }
}
