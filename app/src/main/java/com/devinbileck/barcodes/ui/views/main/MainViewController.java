package com.devinbileck.barcodes.ui.views.main;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotNull;

import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import com.devinbileck.barcodes.ui.views.generatebarcode.GenerateBarcodeViewController;
import com.devinbileck.barcodes.ui.views.scanbarcode.ScanBarcodeViewController;

import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("MainView.fxml")
public class MainViewController {
    private final MainViewModel viewModel;
    private final FxControllerAndView<GenerateBarcodeViewController, BorderPane> generateBarcodeDialog;
    private final FxControllerAndView<ScanBarcodeViewController, BorderPane> scanBarcodeDialog;

    @FXML
    private ListView<BarcodeListItem> listView;

    @FXML
    public VBox detailsPanel;

    @FXML
    public Label detailsBarcodeFormatLabel;

    @FXML
    public Label detailsBarcodeTimestampLabel;

    @FXML
    public Label detailsBarcodeContentLabel;

    @FXML
    public ImageView detailsBarcodeImageView;

    @FXML
    private Button generateBarcodeButton;

    @FXML
    private Button scanBarcodeButton;

    @FXML
    private Button clearBarcodesButton;

    /**
     * Injection of {@link FxControllerAndView} is handled by FxWeaver. Your IDE might get confused,
     * but it works :)
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MainViewController(@NotNull final MainViewModel viewModel,
            @NotNull final FxControllerAndView<ScanBarcodeViewController, BorderPane> scanBarcodeDialog,
            @NotNull final FxControllerAndView<GenerateBarcodeViewController, BorderPane> generateBarcodeDialog) {
        this.viewModel = viewModel;
        this.generateBarcodeDialog = generateBarcodeDialog;
        this.scanBarcodeDialog = scanBarcodeDialog;
    }

    @FXML
    public void initialize() {
        listView.setItems(viewModel.getBarcodes());
        listView.setCellFactory(lv -> {
            final ListCell<BarcodeListItem> cell = new ListCell<>();

            final MenuItem copyMenuItem = new MenuItem("Copy");
            copyMenuItem.setOnAction(event -> {
                ClipboardContent content = new ClipboardContent();
                content.putString(cell.getItem().content());
                Clipboard.getSystemClipboard().setContent(content);
            });

            final MenuItem deleteMenuItem = new MenuItem("Delete");
            deleteMenuItem.setOnAction(event -> listView.getItems().remove(cell.getItem()));

            final ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(copyMenuItem, deleteMenuItem);

            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                BarcodeListItem item = cell.itemProperty().get();
                if (item == null) {
                    return null;
                }
                return item.content();
            }, cell.itemProperty()));
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (Boolean.TRUE.equals(isNowEmpty)) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            cell.setOnMouseClicked(event -> {
                BarcodeListItem item = cell.getItem();
                if (item != null) {
                    detailsBarcodeFormatLabel.setText(item.format().toString());
                    detailsBarcodeContentLabel.setText(item.content());
                    detailsBarcodeImageView.setImage(SwingFXUtils.toFXImage(item.image(), null));
                    detailsBarcodeTimestampLabel.setText(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
                            Instant.ofEpochMilli(item.timestamp()).atZone(ZoneId.systemDefault())));
                }
            });
            return cell;
        });

        detailsPanel.visibleProperty()
                .bind(Bindings.createBooleanBinding(() -> !listView.getSelectionModel().getSelectedItems().isEmpty(),
                        listView.getSelectionModel().getSelectedItems()));

        generateBarcodeButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) generateBarcodeButton.getScene().getWindow();
            generateBarcodeDialog.getController().show(stage, result -> viewModel.addBarcode(
                    new BarcodeListItem(result.format(), result.text(), Instant.now().toEpochMilli(), result.image())));
        });

        scanBarcodeButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) scanBarcodeButton.getScene().getWindow();
            scanBarcodeDialog.getController().show(stage, result -> viewModel.addBarcode(
                    new BarcodeListItem(result.format(), result.text(), Instant.now().toEpochMilli(), result.image())));
        });

        clearBarcodesButton.disableProperty()
                .bind(Bindings.createBooleanBinding(() -> viewModel.getBarcodes().isEmpty(), viewModel.getBarcodes()));
        clearBarcodesButton.setOnAction(actionEvent -> viewModel.clearBarcodes());
    }
}
