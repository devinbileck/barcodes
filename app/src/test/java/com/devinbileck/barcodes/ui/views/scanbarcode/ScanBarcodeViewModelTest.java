package com.devinbileck.barcodes.ui.views.scanbarcode;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import com.devinbileck.barcodes.test.fakes.WebcamDriverFake;
import javafx.application.Platform;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;

import com.devinbileck.barcodes.test.extensions.JavaFxToolkitExtension;
import com.devinbileck.barcodes.test.statements.GivenAWebcamService;
import com.devinbileck.barcodes.test.statements.WhenTheWebcamService;
import com.devinbileck.barcodes.webcam.WebcamDeviceFake;
import com.devinbileck.barcodes.webcam.WebcamDiscoveryService;
import com.devinbileck.barcodes.webcam.WebcamFake;
import com.devinbileck.barcodes.webcam.WebcamService;

@ExtendWith(JavaFxToolkitExtension.class)
class ScanBarcodeViewModelTest {
    private final BufferedImage bufferedImage = new BufferedImage(640, 480, TYPE_INT_RGB);
    private final WebcamService webcamService = new WebcamService();
    private final WebcamDiscoveryService webcamDiscoveryService = new WebcamDiscoveryService();
    private final ScanBarcodeViewModel viewModel =
            new ScanBarcodeViewModel(webcamService, webcamDiscoveryService);
    private final GivenAWebcamService givenAWebcamService = new GivenAWebcamService(webcamService);
    private final WhenTheWebcamService whenTheWebcamService =
            new WhenTheWebcamService(webcamService);

    @BeforeAll
    static void init() {
        System.setProperty("simulation.enabled", "false");
        Webcam.setDriver(new WebcamDriverFake());
    }

    @AfterEach
    void cleanup() {
        Platform.runLater(webcamService::cancel);
    }

    @Test
    void uponInitialization_noWebcamIsSelected() {
        assertThat(viewModel.getSelectedWebcam().get()).isNull();
    }

    @Test
    void whenWebcamServiceValueUpdates_webcamImageUpdates() {
        assertThat(viewModel.getWebcamImage().getValue()).isNull();
        givenAWebcamService.withFunctioningWebcamDevice(bufferedImage);
        whenTheWebcamService.isStarted().andTheValueUpdates();
        assertThat(viewModel.getWebcamImage().getValue()).isNotNull();
    }

    @Test
    void discoveringNoAvailableWebcams_updatesEmptyDiscoveredWebcams() {
        List<Webcam> webcams = Collections.emptyList();
        try (MockedStatic<Webcam> mockedWebcamClass = mockStatic(Webcam.class)) {
            mockedWebcamClass.when(Webcam::getWebcams).thenReturn(webcams);
            viewModel.discoverAvailableWebcams();
        }
        assertThat(viewModel.getAvailableWebcams()).hasSameSizeAs(webcams);
    }

    @Test
    void discoveringAvailableWebcams_updatesDiscoveredWebcams() {
        WebcamDevice webcamDevice = new WebcamDeviceFake(bufferedImage);
        Webcam webcam = new WebcamFake(webcamDevice);
        List<Webcam> webcams = Collections.singletonList(webcam);
        try (MockedStatic<Webcam> mockedWebcamClass = mockStatic(Webcam.class)) {
            mockedWebcamClass.when(Webcam::getWebcams).thenReturn(webcams);
            viewModel.discoverAvailableWebcams();
        }
        assertThat(viewModel.getAvailableWebcams()).hasSameSizeAs(webcams);
    }
}
