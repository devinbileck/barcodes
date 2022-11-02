package com.devinbileck.barcodes.components.elements;

import static org.assertj.core.api.Assertions.assertThat;

import com.devinbileck.barcodes.components.views.View;

public class TextField extends Element<javafx.scene.control.TextField> {
    public TextField(View view, String query) {
        super(javafx.scene.control.TextField.class, view, query);
    }

    public void setText(String text) {
        view.interact(() -> {
            view.getWindow().lookup(query).queryTextInputControl().setText(text);
            return null;
        });
    }

    public void assertHasPromptText(String text) {
        assertThat(view.getWindow().lookup(query).queryTextInputControl().getPromptText()).describedAs(
                "TextField \"%s\" on view \"%s\" has prompt text", query, view.getStageTitle()).isEqualTo(text);
    }

    public void assertIsEmpty() {
        assertThat(view.getWindow().lookup(query).queryTextInputControl().getText()).describedAs(
                "TextField \"%s\" on view \"%s\" is empty", query, view.getStageTitle()).isEmpty();
    }

    public void assertIsNotEmpty() {
        assertThat(view.getWindow().lookup(query).queryTextInputControl().getText()).describedAs(
                "TextField \"%s\" on view \"%s\" is not empty", query, view.getStageTitle()).isNotEmpty();
    }
}
