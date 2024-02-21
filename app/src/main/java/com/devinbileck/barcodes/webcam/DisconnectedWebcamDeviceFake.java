package com.devinbileck.barcodes.webcam;

import javax.validation.constraints.NotNull;

import com.github.sarxos.webcam.WebcamDevice;

public class DisconnectedWebcamDeviceFake extends WebcamDeviceFake {

    /**
     * Simulates a disconnected {@link WebcamDevice}.
     *
     * <p>It will return null when calling {@link #getImage} after a delay has elapsed, by default
     * {@value DEFAULT_GET_IMAGE_DELAY_MILLIS} milliseconds. If {@link #getImage} is called before
     * the delay has elapsed, it will return an image showing the number of milliseconds until the
     * delay will elapse.
     *
     * <p>The purpose of having a delay is to simulate time taken for the user to show a barcode to
     * the camera.
     */
    public DisconnectedWebcamDeviceFake() {
        this(DEFAULT_GET_IMAGE_DELAY_MILLIS);
    }

    /**
     * Simulates a disconnected {@link WebcamDevice}.
     *
     * <p>It will return null when calling {@link #getImage} after the specified getImageDelayMillis
     * delay has elapsed. If {@link #getImage} is called before the delay has elapsed, it will return
     * an image showing the number of milliseconds until the delay will elapse.
     *
     * <p>The purpose of having a delay is to simulate time taken for the user to show a barcode to
     * the camera.
     *
     * @param getImageDelayMillis The number of milliseconds before the image will be returned when
     *     calling {@link #getImage}.
     */
    public DisconnectedWebcamDeviceFake(final long getImageDelayMillis) {
        this("DisconnectedWebcamDeviceFake", getImageDelayMillis);
    }

    /**
     * Simulates a disconnected {@link WebcamDevice}.
     *
     * <p>It will return null when calling {@link #getImage} after the specified getImageDelayMillis
     * delay has elapsed. If {@link #getImage} is called before the delay has elapsed, it will return
     * an image showing the number of milliseconds until the delay will elapse.
     *
     * <p>The purpose of having a delay is to simulate time taken for the user to show a barcode to
     * the camera.
     *
     * @param deviceName A custom descriptor to represent this device.
     * @param getImageDelayMillis The number of milliseconds before the image will be returned when
     *     calling {@link #getImage}.
     */
    public DisconnectedWebcamDeviceFake(@NotNull final String deviceName, final long getImageDelayMillis) {
        super(deviceName, null, getImageDelayMillis);
    }
}
