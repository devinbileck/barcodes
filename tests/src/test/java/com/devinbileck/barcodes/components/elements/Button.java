package com.devinbileck.barcodes.components.elements;

import static org.testfx.assertions.api.Assertions.assertThat;

import com.devinbileck.barcodes.components.views.View;

public class Button extends Element<javafx.scene.control.Button> {
    public Button(View view, String query) {
        super(javafx.scene.control.Button.class, view, query);
    }

    public void click() {
        view.getWindow().clickOn(query);
    }

    public void assertHasText(String text) {
        assertThat(view.getWindow().lookup(query).queryButton())
                .describedAs("Button \"%s\" on view \"%s\" has text", query, view.getStageTitle())
                .hasText(text);
    }
}
