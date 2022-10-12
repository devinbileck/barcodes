package com.devinbileck.barcodes.test.statements;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.concurrent.Worker;

import com.devinbileck.barcodes.test.utils.JavaFxServiceUtil;
import com.devinbileck.barcodes.test.utils.JavaFxThreadUtil;
import com.devinbileck.barcodes.webcam.WebcamService;

public class ThenTheWebcamService {
    private final WebcamService webcamService;
    private final JavaFxServiceUtil<BufferedImage> webcamServiceUtil;

    public ThenTheWebcamService(WebcamService webcamService) {
        this.webcamService = webcamService;
        this.webcamServiceUtil = new JavaFxServiceUtil<>(webcamService);
    }

    public ThenTheWebcamService failsWithException(Exception expectedException) {
        JavaFxServiceUtil.ServiceStatus<BufferedImage> webcamServiceStatus =
                webcamServiceUtil.waitForServiceDesiredState(Worker.State.FAILED);
        assertThat(webcamServiceStatus.exception().getClass())
                .isEqualTo(expectedException.getClass());
        return this;
    }

    public void hasValue(BufferedImage value)
            throws ExecutionException, InterruptedException, TimeoutException {
        JavaFxThreadUtil.runInFxThread(() -> assertThat(webcamService.getValue()).isEqualTo(value));
    }
}
