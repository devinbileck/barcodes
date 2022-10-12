package com.devinbileck.barcodes.ui.views.scanbarcode;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import org.springframework.stereotype.Component;

import com.github.sarxos.webcam.Webcam;

import com.devinbileck.barcodes.webcam.WebcamDiscoveryService;
import com.devinbileck.barcodes.webcam.WebcamService;

@Component
public class ScanBarcodeViewModel {
    private final WebcamDiscoveryService webcamDiscoveryService;
    private final ObservableList<WebcamListItem> availableWebcams =
            FXCollections.observableArrayList();
    private final ObjectProperty<WebcamListItem> selectedWebcam = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> webcamImage = new SimpleObjectProperty<>();

    public ScanBarcodeViewModel(
            WebcamService webcamService, WebcamDiscoveryService webcamDiscoveryService) {
        this.webcamDiscoveryService = webcamDiscoveryService;
        webcamService
                .valueProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue == null) {
                                webcamImage.setValue(null);
                                return;
                            }
                            webcamImage.setValue(SwingFXUtils.toFXImage(newValue, null));
                        });
    }

    public ObjectProperty<WebcamListItem> getSelectedWebcam() {
        return selectedWebcam;
    }

    public void setSelectedWebcam(WebcamListItem webcamListItem) {
        selectedWebcam.setValue(webcamListItem);
    }

    public ObjectProperty<Image> getWebcamImage() {
        return webcamImage;
    }

    public void clearWebcamImage() {
        webcamImage.setValue(null);
    }

    public void discoverAvailableWebcams() {
        availableWebcams.clear();
        for (Webcam webcam : webcamDiscoveryService.getWebcams()) {
            availableWebcams.add(new WebcamListItem(webcam));
        }
    }

    public ObservableList<WebcamListItem> getAvailableWebcams() {
        return availableWebcams;
    }
}
