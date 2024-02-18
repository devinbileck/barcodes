package com.devinbileck.barcodes.webcam;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import javafx.concurrent.Task;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamResolution;
import org.springframework.stereotype.Service;

@Service
public class WebcamService extends javafx.concurrent.Service<BufferedImage> {
    private static final WebcamResolution DEFAULT_RESOLUTION = WebcamResolution.VGA;
    private Webcam webcam;
    private Dimension resolution;

    @Override
    protected Task<BufferedImage> createTask() {
        return new Task<>() {
            @Override
            protected BufferedImage call() {
                updateValue(null);
                try {
                    webcam.setViewSize(resolution);
                    webcam.open();
                    while (!isCancelled()) {
                        if (webcam.isImageNew()) {
                            BufferedImage image;
                            if ((image = webcam.getImage()) == null) {
                                throw new WebcamException("Failed to get image");
                            }
                            updateValue(image);
                        }
                    }
                } finally {
                    webcam.close();
                    updateValue(null);
                }
                return getValue();
            }
        };
    }

    public Webcam getWebcam() {
        return webcam;
    }

    public void setWebcam(final Webcam webcam) {
        if (isRunning()) {
            throw new IllegalStateException("Service must be stopped in order to set webcam");
        }
        if (this.webcam != null && this.webcam.isOpen()) {
            this.webcam.close();
        }
        this.webcam = webcam;
        this.resolution =
                Arrays.stream(webcam.getDevice().getResolutions())
                        .max(new DimensionComparator())
                        .orElse(DEFAULT_RESOLUTION.getSize());
    }

    public Dimension getResolution() {
        return resolution;
    }

    public void setResolution(final Dimension resolution) {
        if (isRunning()) {
            throw new IllegalStateException("Service must be stopped in order to set resolution");
        }
        this.resolution = resolution;
    }

    private static class DimensionComparator implements Comparator<Dimension>, Serializable {
        private static double area(Dimension d) {
            return d.getWidth() * d.getHeight();
        }

        public int compare(Dimension d1, Dimension d2) {
            return Double.compare(area(d1), area(d2));
        }
    }
}
