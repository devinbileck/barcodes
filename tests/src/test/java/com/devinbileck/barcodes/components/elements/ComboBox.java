package com.devinbileck.barcodes.components.elements;

import static org.testfx.assertions.api.Assertions.assertThat;

import com.devinbileck.barcodes.components.views.View;

public class ComboBox extends Element<javafx.scene.control.ComboBox> {
    public ComboBox(View view, String query) {
        super(javafx.scene.control.ComboBox.class, view, query);
    }

    public void select(int index) {
        view.interact(
                () -> {
                    view.getWindow()
                            .lookup(query)
                            .queryComboBox()
                            .getSelectionModel()
                            .select(index);
                    return null;
                });
    }

    public void assertHasPromptText(String text) {
        assertThat(view.getWindow().lookup(query).queryComboBox().getPromptText())
                .describedAs(
                        "ComboBox \"%s\" on view \"%s\" has prompt text",
                        query, view.getStageTitle())
                .isEqualTo(text);
    }

    public void assertIsEmpty() {
        assertThat(view.getWindow().lookup(query).queryComboBox().getItems())
                .describedAs("ComboBox \"%s\" on view \"%s\" is empty", query, view.getStageTitle())
                .isEmpty();
    }

    public void assertIsNotEmpty() {
        assertThat(view.getWindow().lookup(query).queryComboBox().getItems())
                .describedAs("ComboBox \"%s\" on view \"%s\" is empty", query, view.getStageTitle())
                .isNotEmpty();
    }

    public void assertContainsExactlyItemsInOrder(String... text) {
        assertThat(view.getWindow().lookup(query).queryComboBox())
                .describedAs(
                        "ComboBox \"%s\" on view \"%s\" contains exactly items in order %s",
                        query, view.getStageTitle(), text)
                .containsExactlyItemsInOrder(text);
    }
}
