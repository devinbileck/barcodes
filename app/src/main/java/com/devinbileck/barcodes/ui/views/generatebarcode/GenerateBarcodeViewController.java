package com.devinbileck.barcodes.ui.views.generatebarcode;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import com.devinbileck.barcodes.barcode.BarcodeGenerator;
import com.devinbileck.barcodes.barcode.BarcodeResult;

import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("GenerateBarcodeView.fxml")
public class GenerateBarcodeViewController {
    private static final Logger LOG = LoggerFactory.getLogger(GenerateBarcodeViewController.class);

    private final GenerateBarcodeViewModel viewModel;

    private Stage stage;
    private Consumer<BarcodeResult> onGenerated;

    @FXML
    private BorderPane root;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private ComboBox<BarcodeFormatListItem> barcodeTypeComboBox;
    @FXML
    private Button generateButton;
    @FXML
    public Label errorLabel;
    @FXML
    private FlowPane imagePane;
    @FXML
    private ImageView imageView;
    @FXML
    public FlowPane bottomPane;
    @FXML
    public Button addButton;
    @FXML
    public Button closeButton;

    public GenerateBarcodeViewController(GenerateBarcodeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void initialize() {
        initializeStage();

        barcodeTypeComboBox.setPromptText("Select Format");
        barcodeTypeComboBox.setItems(viewModel.getAvailableBarcodeFormats());
        barcodeTypeComboBox.valueProperty().bindBidirectional(viewModel.getSpecifiedFormat());

        // In order for the combo box to show the prompt text after setting its value to null,
        // need to override the ListCell updateItem method.
        barcodeTypeComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(BarcodeFormatListItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(barcodeTypeComboBox.getPromptText());
                } else {
                    setText(item.toString());
                }
            }
        });

        contentTextArea.setPromptText("Enter Content");
        contentTextArea.textProperty().bindBidirectional(viewModel.getSpecifiedContent());

        generateButton.disableProperty().bind(viewModel.canGenerate().not());
        generateButton.setOnAction(actionEvent -> {
            final String barcodeContent = contentTextArea.getText();
            final BarcodeFormat barcodeFormat = barcodeTypeComboBox.getValue().barcodeFormat();
            final int imageWidth = (int) imagePane.getWidth();
            final int imageHeight = (int) imagePane.getHeight();
            BufferedImage generatedImage;
            try {
                generatedImage =
                        BarcodeGenerator.generateBarcodeImage(barcodeContent, barcodeFormat, imageWidth, imageHeight)
                                .image();
                viewModel.setGeneratedResult(
                        Optional.of(new BarcodeResult(barcodeFormat, barcodeContent, generatedImage)));
                errorLabel.setText("");
            } catch (WriterException | IllegalArgumentException e) {
                LOG.error("Unable to generate barcode of type [{}]: {}", barcodeFormat, e.getMessage());
                errorLabel.setText(e.getMessage());
                viewModel.setGeneratedResult(Optional.empty());
            }
        });

        imageView.imageProperty().bind(Bindings.createObjectBinding(() -> {
            Optional<BarcodeResult> result = viewModel.getGeneratedResult().get();
            if (result.isEmpty()) {
                return null;
            }
            BufferedImage image = result.get().image();
            if (image != null) {
                return SwingFXUtils.toFXImage(image, null);
            }
            return null;
        }, viewModel.getGeneratedResult()));

        addButton.disableProperty().bind(Bindings.createBooleanBinding(() -> viewModel.getGeneratedResult().get()
                        .map(barcodeResult -> barcodeResult.text() == null || barcodeResult.text().isEmpty()).orElse(true),
                viewModel.getGeneratedResult()));
        addButton.setOnAction(actionEvent -> {
            close();
            onGenerated.accept(viewModel.getGeneratedResult().get().orElseThrow());
        });

        closeButton.setOnAction(actionEvent -> close());
    }

    private void initializeStage() {
        this.stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Generate Barcode");
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
    }

    public void show(Stage parentStage, Consumer<BarcodeResult> onGenerated) {
        this.onGenerated = onGenerated;
        try {
            stage.initOwner(parentStage);
        } catch (IllegalStateException ignored) {
            // Cannot set owner once stage has been set visible
        }
        viewModel.setSpecifiedContent("");
        viewModel.setSpecifiedFormat(null);
        viewModel.setGeneratedResult(Optional.empty());
        stage.show();

        // Must set the minimum width and height of the stage after showing it,
        // otherwise it will be ignored
        stage.setMinHeight(250);
        stage.setMinWidth(400);
    }

    public void close() {
        stage.close();
    }
}
