package com.devinbileck.barcodes.webcam;

import static com.devinbileck.barcodes.test.utils.JavaFxThreadUtil.runInFxThread;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamException;
import com.github.sarxos.webcam.WebcamResolution;

import com.devinbileck.barcodes.test.extensions.JavaFxToolkitExtension;
import com.devinbileck.barcodes.test.fakes.WebcamDriverFake;
import com.devinbileck.barcodes.test.statements.GivenAWebcamService;
import com.devinbileck.barcodes.test.statements.ThenTheWebcamService;
import com.devinbileck.barcodes.test.statements.WhenTheWebcamService;
import com.devinbileck.barcodes.test.utils.JavaFxServiceUtil;

@ExtendWith(JavaFxToolkitExtension.class)
@DisabledIf(value = "java.awt.GraphicsEnvironment#isHeadless",
        disabledReason = "Headless environment is unable to open DISPLAY while running in Fx thread")
class WebcamServiceTest {
    private final BufferedImage bufferedImage = new BufferedImage(640, 480, TYPE_INT_RGB);
    private final WebcamService webcamService = new WebcamService();
    private final GivenAWebcamService givenAWebcamService = new GivenAWebcamService(webcamService);
    private final WhenTheWebcamService whenTheWebcamService =
            new WhenTheWebcamService(webcamService);
    private final ThenTheWebcamService thenTheWebcamService =
            new ThenTheWebcamService(webcamService);

    @BeforeAll
    static void setDriver() {
        Webcam.setDriver(new WebcamDriverFake());
    }

    @AfterEach
    void cleanup() {
        Platform.runLater(webcamService::cancel);
    }

    @Test
    void startingServiceWithoutWebcamSpecified_serviceFailsWithException() {
        givenAWebcamService.withNoWebcam();
        whenTheWebcamService.isStarted();
        thenTheWebcamService.failsWithException(new NullPointerException());
    }

    @Test
    void startingServiceWhenWebcamDeviceFailsToOpen_serviceFailsWithException() {
        RuntimeException thrownException = new WebcamException("Webcam exception when opening");
        givenAWebcamService.withNonOpeningWebcamDevice(thrownException);
        whenTheWebcamService.isStarted();
        thenTheWebcamService.failsWithException(thrownException);
    }

    @Test
    void startingServiceWhenWebcamDeviceIsDisconnected_serviceFailsWithException()
            throws ExecutionException, InterruptedException, TimeoutException {
        RuntimeException thrownException = new WebcamException("Failed to get image");
        givenAWebcamService.withDisconnectedWebcamDevice();
        whenTheWebcamService.isStarted();
        thenTheWebcamService.failsWithException(thrownException).hasValue(null);
    }

    @Test
    void startingServiceWithFunctioningWebcam_serviceUpdatesValue() {
        givenAWebcamService.withFunctioningWebcamDevice(bufferedImage);
        JavaFxServiceUtil.ServiceStatus<BufferedImage> webcamServiceStatus =
                whenTheWebcamService.isStarted().andTheValueUpdates();
        assertThat(webcamServiceStatus.value().getWidth())
                .describedAs("Image width")
                .isEqualTo(bufferedImage.getWidth());
        assertThat(webcamServiceStatus.value().getHeight())
                .describedAs("Image height")
                .isEqualTo(bufferedImage.getHeight());
    }

    @Test
    void changingWebcamWhileServiceIsRunning_throwsException()
            throws ExecutionException, InterruptedException, TimeoutException {
        Webcam newWebcam = mock(Webcam.class);
        givenAWebcamService.withFunctioningWebcamDevice(bufferedImage);
        whenTheWebcamService.isStarted().andTheStateChangesToRunning();
        runInFxThread(
                () ->
                        assertThrows(
                                IllegalStateException.class,
                                () -> webcamService.setWebcam(newWebcam)));
    }

    @Test
    void changingWebcamWhileServiceIsStopped_changesWebcam()
            throws ExecutionException, InterruptedException, TimeoutException {
        WebcamDevice newWebcamDevice = new WebcamDeviceFake(bufferedImage);
        Webcam newWebcam = new WebcamFake(newWebcamDevice);
        givenAWebcamService.withFunctioningWebcamDevice(bufferedImage);
        runInFxThread(
                () -> {
                    Webcam currentWebcam = webcamService.getWebcam();
                    currentWebcam.open();
                    webcamService.setWebcam(newWebcam);
                    assertThat(webcamService.getWebcam()).isEqualTo(newWebcam);
                    assertThat(currentWebcam.isOpen()).isFalse();
                    assertThat(newWebcam.isOpen()).isFalse();
                });
    }

    @Test
    void changingResolutionWhileServiceIsRunning_throwsException()
            throws ExecutionException, InterruptedException, TimeoutException {
        Dimension newResolution = WebcamResolution.HD.getSize();
        givenAWebcamService.withFunctioningWebcamDevice(bufferedImage);
        whenTheWebcamService.isStarted().andTheStateChangesToRunning();
        runInFxThread(
                () ->
                        assertThrows(
                                IllegalStateException.class,
                                () -> webcamService.setResolution(newResolution)));
    }

    @Test
    void changingResolutionWhileServiceIsStopped_changesResolution()
            throws ExecutionException, InterruptedException, TimeoutException {
        Dimension newResolution = WebcamResolution.HD.getSize();
        givenAWebcamService.withFunctioningWebcamDevice(bufferedImage);
        runInFxThread(
                () -> {
                    webcamService.setResolution(newResolution);
                    assertThat(webcamService.getResolution()).isEqualTo(newResolution);
                });
    }

    @Test
    void cancellingServiceAfterValueUpdated_valueIsNullAndWebcamIsClosed()
            throws ExecutionException, InterruptedException, TimeoutException {
        givenAWebcamService.withFunctioningWebcamDevice(bufferedImage);
        whenTheWebcamService.isStarted().andTheValueUpdates();
        whenTheWebcamService.isStopped().andTheValueIsNull();
        runInFxThread(
                () -> {
                    assertThat(webcamService.getWebcam().isOpen()).isFalse();
                    assertThat(webcamService.getWebcam().getDevice().isOpen()).isFalse();
                });
    }

    @Test
    void checkingValueBeforeValueUpdated_valueIsNull()
            throws ExecutionException, InterruptedException, TimeoutException {
        givenAWebcamService.withFunctioningWebcamDevice(bufferedImage);
        whenTheWebcamService.isStarted();
        thenTheWebcamService.hasValue(null);
    }
}
