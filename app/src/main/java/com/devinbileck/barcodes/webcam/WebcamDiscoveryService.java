package com.devinbileck.barcodes.webcam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import com.google.zxing.WriterException;

import com.devinbileck.barcodes.barcode.BarcodeGenerator;
import com.devinbileck.barcodes.config.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebcamDiscoveryService {
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
            qrCodeImage = BarcodeGenerator.generateQRCodeImage(new Date().toString());
        } catch (WriterException e) {
            log.error("Failed to generate QR image", e);
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
