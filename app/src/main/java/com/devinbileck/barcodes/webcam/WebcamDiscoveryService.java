package com.devinbileck.barcodes.webcam;

import com.devinbileck.barcodes.barcode.BarcodeGenerator;
import com.devinbileck.barcodes.config.Configuration;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class WebcamDiscoveryService {
    private static final Logger LOG = LoggerFactory.getLogger(WebcamDiscoveryService.class);

    public List<Webcam> getWebcams() {
        if (!Configuration.isSimulationEnabled()) {
            return Webcam.getWebcams();
        }
        if (Configuration.isSimulationFakeWebcams()) {
            return simulatedWebcams();
        }
        return Collections.emptyList();
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
        webcams.add(new WebcamFake(new WebcamDeviceFake(qrCodeImage)));
        webcams.add(new WebcamFake(new DisconnectedWebcamDeviceFake()));
        webcams.add(
                new WebcamFake(
                        new NonOpeningWebcamDeviceFake(
                                new WebcamException("Webcam exception when opening"))));
        return webcams;
    }
}
