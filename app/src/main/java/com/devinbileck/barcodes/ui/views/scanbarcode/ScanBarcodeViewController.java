package com.devinbileck.barcodes.ui.views.scanbarcode;

import com.devinbileck.barcodes.barcode.BarcodeProcessor;
import com.devinbileck.barcodes.barcode.BarcodeResult;
import com.devinbileck.barcodes.webcam.WebcamService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@FxmlView("ScanBarcodeView.fxml")
public class ScanBarcodeViewController {
    private static final Logger LOG = LoggerFactory.getLogger(ScanBarcodeViewController.class);

    private final ScanBarcodeViewModel viewModel;
    private final WebcamService webcamService;

    private Stage stage;
    private Consumer<BarcodeResult> onScanned;
    private boolean isClosing;

    @FXML
    private BorderPane root;

    @FXML
    private ComboBox<WebcamListItem> webcamComboBox;

    @FXML
    private FlowPane webcamPane;

    @FXML
    private Label webcamStatusLabel;

    @FXML
    private ImageView webcamImageView;

    @FXML
    public FlowPane bottomPane;

    @FXML
    public Button closeButton;

    public ScanBarcodeViewController(ScanBarcodeViewModel viewModel, WebcamService webcamService) {
        this.viewModel = viewModel;
        this.webcamService = webcamService;
    }

    @FXML
    public void initialize() {
        initializeStage();

        initializeWebcamComboBox();

        webcamImageView.imageProperty().bind(viewModel.getWebcamImage());

        initializeWebcamStatusLabel();

        webcamService.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isClosing || newValue == null) {
                return;
            }
            BarcodeProcessor.process(newValue).ifPresent(result -> {
                close();
                onScanned.accept(result);
            });
        });

        closeButton.setOnAction(actionEvent -> close());
    }

    private void initializeStage() {
        this.stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Scan Barcode");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setOnCloseRequest(event -> cleanup());
        stage.widthProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::updateView));
        stage.heightProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(this::updateView));
    }

    private void initializeWebcamComboBox() {
        webcamComboBox.setPromptText("Choose Camera");
        webcamComboBox.disableProperty()
                .bind(Bindings.createBooleanBinding(() -> viewModel.getAvailableWebcams().isEmpty(),
                        viewModel.getAvailableWebcams()));
        webcamComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                viewModel.clearWebcamImage();
                startWebcam(newValue);
            }
        });
    }

    private void initializeWebcamStatusLabel() {
        webcamService.stateProperty().addListener((observable, oldState, newState) -> {
            switch (newState) {
                case SCHEDULED -> webcamStatusLabel.setText("Waiting...");
                case FAILED -> {
                    String exception;
                    if (webcamService.getException().getCause() != null) {
                        exception = String.valueOf(webcamService.getException().getCause());
                    } else {
                        exception = String.valueOf(webcamService.getException());
                    }
                    LOG.error(exception);
                    if (exception.contains("Failed to get image")) {
                        webcamStatusLabel.setText("Failed to get image from webcam. Is it" + " disconnected?");
                    } else {
                        webcamStatusLabel.setText("Failed to load webcam");
                    }
                }
                case CANCELLED -> webcamStatusLabel.setText("Stopped");
                default -> webcamStatusLabel.setText("");
            }
            Platform.runLater(this::updateView);
        });
    }

    public void show(Stage parentStage, Consumer<BarcodeResult> onScanned) {
        this.onScanned = onScanned;
        isClosing = false;
        try {
            stage.initOwner(parentStage);
        } catch (IllegalStateException ignored) {
            // Cannot set owner once stage has been set visible
        }
        viewModel.discoverAvailableWebcams();
        webcamComboBox.setItems(viewModel.getAvailableWebcams());
        if (viewModel.getSelectedWebcam().getValue() != null) {
            webcamComboBox.getSelectionModel().select(viewModel.getSelectedWebcam().getValue());
        }
        stage.show();

        // Must set the minimum width and height of the stage after showing it,
        // otherwise it will be ignored
        stage.setMinHeight(400);
        stage.setMinWidth(400);
    }

    public void cleanup() {
        isClosing = true;
        Platform.runLater(() -> {
            webcamService.cancel();
            webcamService.reset();
        });
    }

    public void close() {
        cleanup();
        stage.close();
    }

    private void updateView() {
        double width = webcamPane.getWidth();
        double height = webcamPane.getHeight();
        if (webcamService.isRunning()) {
            // The height is offset by the bottom pane height in order to ensure the bottom pane is
            // always visible.
            // This may be a hack, but I have yet to find another working solution.
            webcamImageView.setFitWidth(width);
            webcamImageView.setFitHeight(height - bottomPane.getHeight());
            webcamImageView.prefWidth(height);
            webcamImageView.prefHeight(width - bottomPane.getHeight());
        } else {
            webcamImageView.setFitWidth(0);
            webcamImageView.setFitHeight(0);
            webcamImageView.prefWidth(0);
            webcamImageView.prefHeight(0);
        }
    }

    private void startWebcam(WebcamListItem newValue) {
        Platform.runLater(() -> {
            webcamService.cancel();
            webcamService.setWebcam(newValue.webcam());
            viewModel.setSelectedWebcam(newValue);
            webcamService.restart();
            updateView();
        });
    }
}
