package com.devinbileck.barcodes.components.views.main;

import com.devinbileck.barcodes.components.elements.Button;
import com.devinbileck.barcodes.components.elements.ListView;
import com.devinbileck.barcodes.components.views.View;

public class MainView extends View {
    private static final String STAGE_TITLE = "Barcodes";

    public final ListView listView = new ListView(this, "#listView");
    public final Button generateBarcodeButton = new Button(this, "#generateBarcodeButton");
    public final Button scanBarcodeButton = new Button(this, "#scanBarcodeButton");
    public final Button clearBarcodesButton = new Button(this, "#clearBarcodesButton");

    private static MainView instance = null;

    private MainView() {
        // this class cannot be instantiated
    }

    public static MainView getInstance() {
        if (instance == null) {
            instance = new MainView();
        }
        return instance;
    }

    @Override
    public String getStageTitle() {
        return STAGE_TITLE;
    }
}
