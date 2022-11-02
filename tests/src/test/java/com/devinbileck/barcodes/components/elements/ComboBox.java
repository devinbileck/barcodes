package com.devinbileck.barcodes.components.elements;

import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.devinbileck.barcodes.components.views.View;

public class ComboBox extends Element<javafx.scene.control.ComboBox> {
    public ComboBox(View view, String query) {
        super(javafx.scene.control.ComboBox.class, view, query);
    }

    public void selectIndex(final int index) {
        view.interact(() -> {
            view.getWindow().lookup(query).queryComboBox().getSelectionModel().select(index);
            return null;
        });
    }

    public void selectItem(final Object item) {
        view.interact(() -> {
            view.getWindow().lookup(query).queryComboBox().getSelectionModel().select(item);
            return null;
        });
    }

    public int getSelectedIndex() {
        final AtomicInteger index = new AtomicInteger();
        view.interact(() -> {
            index.set(view.getWindow().lookup(query).queryComboBox().getSelectionModel().getSelectedIndex());
            return null;
        });
        return index.get();
    }

    public String getSelectedText() {
        final String[] text = new String[1];
        view.interact(() -> {
            var selectedItem = view.getWindow().lookup(query).queryComboBox().getSelectionModel().getSelectedItem();
            String selectedText = view.getWindow().lookup(query).queryComboBox().getPromptText();
            if (selectedItem != null) {
                selectedText = selectedItem.toString();
            }
            text[0] = selectedText;
            return null;
        });
        return text[0];
    }

    public void assertHasPromptText(String text) {
        assertThat(view.getWindow().lookup(query).queryComboBox().getPromptText()).describedAs(
                "ComboBox \"%s\" on view \"%s\" has prompt text", query, view.getStageTitle()).isEqualTo(text);
    }

    public void assertIsEmpty() {
        assertThat(view.getWindow().lookup(query).queryComboBox().getItems()).describedAs(
                "ComboBox \"%s\" on view \"%s\" is empty", query, view.getStageTitle()).isEmpty();
    }

    public void assertIsNotEmpty() {
        assertThat(view.getWindow().lookup(query).queryComboBox().getItems()).describedAs(
                "ComboBox \"%s\" on view \"%s\" is not empty", query, view.getStageTitle()).isNotEmpty();
    }

    public <T> void assertContainsExactlyItemsInOrder(List<T> items) {
        assertThat(view.getWindow().lookup(query).queryComboBox()).describedAs(
                        "ComboBox \"%s\" on view \"%s\" contains exactly items in order %s", query,
                        view.getStageTitle(), items)
                .containsExactlyItemsInOrder(items.toArray());
    }
}
