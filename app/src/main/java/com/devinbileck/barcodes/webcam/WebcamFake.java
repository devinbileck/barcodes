package com.devinbileck.barcodes.webcam;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;

public class WebcamFake extends Webcam {
    protected static final double FPS = 30.0;
    protected long isImageNewTimestamp = 0;

    public WebcamFake(WebcamDevice device) {
        super(device);
    }

    @Override
    public boolean open() {
        getDevice().open();
        return true;
    }

    @Override
    public boolean close() {
        getDevice().close();
        return true;
    }

    @Override
    public boolean isOpen() {
        return getDevice().isOpen();
    }

    @Override
    public BufferedImage getImage() {
        return getDevice().getImage();
    }

    @Override
    public boolean isImageNew() {
        // Introduce a delay when a new image is available in order to simulate the FPS of the
        // webcam
        if (isImageNewTimestamp == 0) {
            isImageNewTimestamp = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - isImageNewTimestamp < (1 / getFPS()) * 1000) {
            return false;
        }

        isImageNewTimestamp = 0;

        return true;
    }

    @Override
    public double getFPS() {
        return FPS;
    }
}
