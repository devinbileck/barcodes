package com.devinbileck.barcodes.components.views.scanbarcode;

import com.devinbileck.barcodes.components.elements.Button;
import com.devinbileck.barcodes.components.elements.ComboBox;
import com.devinbileck.barcodes.components.elements.ImageView;
import com.devinbileck.barcodes.components.elements.Label;
import com.devinbileck.barcodes.components.views.View;

public class ScanBarcodeView extends View {
    private static final String STAGE_TITLE = "Scan Barcode";

    public final ComboBox webcamComboBox = new ComboBox(this, "#webcamComboBox");
    public final Label webcamStatusLabel = new Label(this, "#webcamStatusLabel");
    public final ImageView webcamImageView = new ImageView(this, "#webcamImageView");
    public final Button closeButton = new Button(this, "#closeButton");

    private static ScanBarcodeView instance = null;

    private ScanBarcodeView() {
        // this class cannot be instantiated
    }

    public static ScanBarcodeView getInstance() {
        if (instance == null) {
            instance = new ScanBarcodeView();
        }
        return instance;
    }

    @Override
    public String getStageTitle() {
        return STAGE_TITLE;
    }
}
