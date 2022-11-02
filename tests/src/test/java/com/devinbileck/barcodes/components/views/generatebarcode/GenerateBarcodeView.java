package com.devinbileck.barcodes.components.views.generatebarcode;

import com.devinbileck.barcodes.components.elements.Button;
import com.devinbileck.barcodes.components.elements.ComboBox;
import com.devinbileck.barcodes.components.elements.ImageView;
import com.devinbileck.barcodes.components.elements.TextArea;
import com.devinbileck.barcodes.components.elements.TextField;
import com.devinbileck.barcodes.components.views.View;

public class GenerateBarcodeView extends View {
    private static final String STAGE_TITLE = "Generate Barcode";

    public final TextArea contentTextArea = new TextArea(this, "#contentTextArea");
    public final ComboBox barcodeTypeComboBox = new ComboBox(this, "#barcodeTypeComboBox");
    public final Button generateButton = new Button(this, "#generateButton");
    public final ImageView imageView = new ImageView(this, "#imageView");
    public final Button closeButton = new Button(this, "#closeButton");

    private static GenerateBarcodeView instance = null;

    private GenerateBarcodeView() {
    }

    public static GenerateBarcodeView getInstance() {
        if (instance == null) {
            instance = new GenerateBarcodeView();
        }
        return instance;
    }

    @Override
    public String getStageTitle() {
        return STAGE_TITLE;
    }
}
