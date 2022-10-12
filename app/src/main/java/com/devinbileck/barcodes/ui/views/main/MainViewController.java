package com.devinbileck.barcodes.ui.views.main;

import javax.validation.constraints.NotNull;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;

import com.devinbileck.barcodes.ui.views.scanbarcode.ScanBarcodeViewController;

import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@FxmlView("MainView.fxml")
public class MainViewController {
    private final MainViewModel viewModel;
    private final FxControllerAndView<ScanBarcodeViewController, BorderPane> scanBarcodeDialog;

    @FXML private ListView<String> listView;

    @FXML private Button scanBarcodeButton;

    @FXML private Button clearScannedContentButton;

    /**
     * Injection of {@link FxControllerAndView} is handled by FxWeaver. Your IDE might get confused,
     * but it works :)
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MainViewController(
            @NotNull final MainViewModel viewModel,
            @NotNull
                    final FxControllerAndView<ScanBarcodeViewController, BorderPane>
                            scanBarcodeDialog) {
        this.viewModel = viewModel;
        this.scanBarcodeDialog = scanBarcodeDialog;
    }

    @FXML
    public void initialize() {
        listView.setItems(viewModel.getScannedContent());
        listView.setCellFactory(
                lv -> {
                    final ListCell<String> cell = new ListCell<>();

                    final MenuItem copyMenuItem = new MenuItem("Copy");
                    copyMenuItem.setOnAction(
                            event -> {
                                ClipboardContent content = new ClipboardContent();
                                content.putString(cell.getItem());
                                Clipboard.getSystemClipboard().setContent(content);
                            });

                    final MenuItem deleteMenuItem = new MenuItem("Delete");
                    deleteMenuItem.setOnAction(event -> listView.getItems().remove(cell.getItem()));

                    final ContextMenu contextMenu = new ContextMenu();
                    contextMenu.getItems().addAll(copyMenuItem, deleteMenuItem);

                    cell.textProperty().bind(cell.itemProperty());
                    cell.emptyProperty()
                            .addListener(
                                    (obs, wasEmpty, isNowEmpty) -> {
                                        if (Boolean.TRUE.equals(isNowEmpty)) {
                                            cell.setContextMenu(null);
                                        } else {
                                            cell.setContextMenu(contextMenu);
                                        }
                                    });
                    return cell;
                });

        scanBarcodeButton.setOnAction(
                actionEvent -> {
                    Stage stage = (Stage) scanBarcodeButton.getScene().getWindow();
                    scanBarcodeDialog.getController().show(stage, viewModel::addScannedContent);
                });

        clearScannedContentButton
                .disableProperty()
                .bind(
                        Bindings.createBooleanBinding(
                                () -> viewModel.getScannedContent().isEmpty(),
                                viewModel.getScannedContent()));
        clearScannedContentButton.setOnAction(actionEvent -> viewModel.clearScannedContent());
    }
}
