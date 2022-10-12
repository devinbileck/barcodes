package com.devinbileck.barcodes.components.elements;

import static org.testfx.assertions.api.Assertions.assertThat;

import com.devinbileck.barcodes.components.views.View;

public class Label extends Element<javafx.scene.control.Label> {
    public Label(View view, String query) {
        super(javafx.scene.control.Label.class, view, query);
    }

    public void assertTextIsEqualTo(String text) {
        assertThat(
                        view.getWindow()
                                .lookup(query)
                                .queryAs(javafx.scene.control.Label.class)
                                .getText())
                .describedAs(
                        "Label \"%s\" on view \"%s\" text is equal to", query, view.getStageTitle())
                .isEqualTo(text);
    }
}
