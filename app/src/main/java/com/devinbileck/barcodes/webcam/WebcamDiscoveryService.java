package com.devinbileck.barcodes.webcam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.google.zxing.WriterException;

import com.devinbileck.barcodes.barcode.BarcodeGenerator;
import com.devinbileck.barcodes.config.Configuration;

@Service
public class WebcamDiscoveryService {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamDiscoveryService.class);
    public static final long SIMULATED_WEBCAM_GET_IMAGE_DELAY_MILLIS = 5_000L;

    public List<Webcam> getWebcams() {
        if (!Configuration.isSimulationEnabled()) {
            return Webcam.getWebcams();
        }
        return Configuration.isSimulationFakeWebcams() ? simulatedWebcams() : Collections.emptyList();
    }

    private List<Webcam> simulatedWebcams() {
        BufferedImage qrCodeImage;
        try {
            qrCodeImage = BarcodeGenerator.generateQRCodeImage(new Date().toString()).image();
        } catch (WriterException e) {
            LOG.error("Failed to generate QR image", e);
            return Collections.emptyList();
        }

        List<Webcam> webcams = new ArrayList<>();
        webcams.add(new WebcamFake(new WebcamDeviceFake(qrCodeImage, SIMULATED_WEBCAM_GET_IMAGE_DELAY_MILLIS)));
        webcams.add(new WebcamFake(new DisconnectedWebcamDeviceFake(SIMULATED_WEBCAM_GET_IMAGE_DELAY_MILLIS)));
        webcams.add(
                new WebcamFake(
                        new NonOpeningWebcamDeviceFake(
                                new WebcamException("Webcam exception when opening"))));
        return webcams;
    }
}
