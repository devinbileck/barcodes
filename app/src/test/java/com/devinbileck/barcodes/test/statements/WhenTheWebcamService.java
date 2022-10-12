package com.devinbileck.barcodes.test.statements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.concurrent.Worker;

import com.devinbileck.barcodes.test.utils.JavaFxServiceUtil;
import com.devinbileck.barcodes.test.utils.JavaFxThreadUtil;
import com.devinbileck.barcodes.webcam.WebcamService;

public class WhenTheWebcamService {
    private final WebcamService webcamService;
    private final JavaFxServiceUtil<BufferedImage> webcamServiceUtil;

    public WhenTheWebcamService(WebcamService webcamService) {
        this.webcamService = webcamService;
        this.webcamServiceUtil = new JavaFxServiceUtil<>(webcamService);
    }

    public WhenTheWebcamService isStarted() {
        assertEquals(Worker.State.READY, webcamService.getState());
        webcamService.start();
        return this;
    }

    public WhenTheWebcamService isStopped()
            throws ExecutionException, InterruptedException, TimeoutException {
        JavaFxThreadUtil.runInFxThread(webcamService::cancel);
        return this;
    }

    public void andTheStateChangesToRunning() {
        webcamServiceUtil.waitForServiceDesiredState(Worker.State.RUNNING);
    }

    public JavaFxServiceUtil.ServiceStatus<BufferedImage> andTheValueUpdates() {
        return webcamServiceUtil.waitForServiceUpdateValue(null);
    }

    public void andTheValueIsNull() {
        webcamServiceUtil.waitForServiceNullValue();
    }
}
