package com.devinbileck.barcodes.webcam;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamResolution;

import com.devinbileck.barcodes.image.ImageUtil;

public class WebcamDeviceFake implements WebcamDevice {
    protected static final long GET_IMAGE_DELAY_MILLIS_DEFAULT = 5000L;
    protected final String deviceName;
    protected final BufferedImage bufferedImage;
    protected final Dimension[] resolutions;
    protected Dimension resolution;
    protected boolean isOpen;
    protected long getImageDelayMillis;
    protected long getImageTimestamp = 0;

    /**
     * Simulates a functioning {@link WebcamDevice}.
     *
     * <p>It will return the provided {@link BufferedImage} when calling {@link #getImage} after a
     * delay has elapsed, by default {@value GET_IMAGE_DELAY_MILLIS_DEFAULT} milliseconds. If {@link
     * #getImage} is called before the delay has elapsed, it will return an image showing the number
     * of milliseconds until the delay will elapse.
     *
     * <p>The purpose of having a delay is to simulate time taken for the user to show a barcode to
     * the camera.
     *
     * @param bufferedImage The image to be returned when calling {@link #getImage} after the
     *     default delay of {@value GET_IMAGE_DELAY_MILLIS_DEFAULT} milliseconds has elapsed.
     */
    public WebcamDeviceFake(BufferedImage bufferedImage) {
        this("FunctioningWebcamDeviceFake", bufferedImage, GET_IMAGE_DELAY_MILLIS_DEFAULT);
    }

    /**
     * Simulates a functioning {@link WebcamDevice}.
     *
     * <p>It will return the provided {@link BufferedImage} when calling {@link #getImage} after the
     * specified getImageDelayMillis delay has elapsed. If {@link #getImage} is called before the
     * delay has elapsed, it will return an image showing the number of milliseconds until the delay
     * will elapse.
     *
     * <p>The purpose of having a delay is to simulate time taken for the user to show a barcode to
     * the camera.
     *
     * @param deviceName A custom descriptor to represent this device.
     * @param bufferedImage The image to be returned when calling {@link #getImage} after the
     *     provided getImageDelayMillis delay has elapsed.
     * @param getImageDelayMillis The number of milliseconds before the image will be returned when
     *     calling {@link #getImage}.
     */
    public WebcamDeviceFake(
            String deviceName, BufferedImage bufferedImage, long getImageDelayMillis) {
        this.deviceName = deviceName;
        this.bufferedImage = bufferedImage;
        this.getImageDelayMillis = getImageDelayMillis;
        if (bufferedImage != null) {
            this.resolution = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
            this.resolutions = new Dimension[] {this.resolution};
        } else {
            this.resolution = WebcamResolution.SXGA.getSize();
            this.resolutions =
                    new Dimension[] {
                        WebcamResolution.QVGA.getSize(),
                        WebcamResolution.VGA.getSize(),
                        WebcamResolution.XGA.getSize(),
                        WebcamResolution.SXGA.getSize()
                    };
        }
    }

    @Override
    public String getName() {
        return deviceName;
    }

    @Override
    public Dimension[] getResolutions() {
        return resolutions;
    }

    @Override
    public Dimension getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Dimension dimension) {
        resolution = dimension;
    }

    @Override
    public BufferedImage getImage() {
        if (getImageTimestamp == 0) {
            getImageTimestamp = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - getImageTimestamp < getImageDelayMillis) {
            String countDownText =
                    String.format(
                            "%s",
                            getImageDelayMillis + getImageTimestamp - System.currentTimeMillis());
            return ImageUtil.generateTextImage(
                    countDownText, (int) resolution.getWidth(), (int) resolution.getHeight());
        }

        getImageTimestamp = 0;

        return bufferedImage;
    }

    @Override
    public void open() {
        isOpen = true;
    }

    @Override
    public void close() {
        isOpen = false;
        getImageTimestamp = 0;
    }

    @Override
    public void dispose() {
        // Do nothing
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }
}
