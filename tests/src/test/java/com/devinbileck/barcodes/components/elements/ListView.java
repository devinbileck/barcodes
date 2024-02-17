package com.devinbileck.barcodes.components.elements;

import static com.devinbileck.barcodes.test.utils.JavaFxThreadUtil.runInFxThread;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.skin.VirtualFlow;

import com.devinbileck.barcodes.components.views.View;

public class ListView extends Element<javafx.scene.control.ListView> {
    public ListView(View view, String query) {
        super(javafx.scene.control.ListView.class, view, query);
    }

    public void addItem(String item)
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(() -> view.getWindow().lookup(query).queryListView().getItems().add(item));
        // TODO Find a better solution than this hack to ensure the UI updates before interacting
        //  with the item
        Thread.sleep(50);
    }

    public void clearItems() throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(() -> view.getWindow().lookup(query).queryListView().getItems().clear());
    }

    public void rightClickCell(int cellIndex)
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(
                () -> {
                    ListCell<?> cell = getListCells().getCell(cellIndex);
                    assertNotNull(cell, "ListCell index not found");

                    view.getWindow().rightClickOn(cell);

                    // TODO Determine if following hack is necessary to get the context menu to
                    // appear when running headless tests
                    //  Remove this once the issue with Monocle is resolved:
                    // https://github.com/TestFX/Monocle/issues/12
                    //            ContextMenu contextMenu = cell.getContextMenu();
                    //            if (!contextMenu.isShowing()) {
                    //                contextMenu.show(view.getWindow().targetWindow());
                    //            }
                });
    }

    public void rightClickCellAndSelectMenuItem(int cellIndex, int menuItemIndex)
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(
                () -> {
                    ListCell<?> cell = getListCells().getCell(cellIndex);
                    assertNotNull(cell, "ListCell index not found");

                    view.getWindow().rightClickOn(cell);

                    ContextMenu contextMenu = cell.getContextMenu();
                    assertNotNull(contextMenu, "ContextMenu not found");

                    // TODO Determine if following hack is necessary to get the context menu to
                    // appear when running headless tests
                    //  Remove this once the issue with Monocle is resolved:
                    // https://github.com/TestFX/Monocle/issues/12
                    //            if (!contextMenu.isShowing()) {
                    //                contextMenu.show(view.getWindow().targetWindow());
                    //            }

                    // TODO find a better solution than this hack to ensure the menu appears before
                    //  interacting with the item
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    MenuItem contextMenuItem = contextMenu.getItems().get(menuItemIndex);
                    assertNotNull(contextMenuItem, "MenuItem index not found");

                    view.getWindow().clickOn(contextMenuItem.getStyleableNode());
                });
    }

    public void closeContextMenu(int cellIndex)
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(
                () -> {
                    ListCell<?> cell = getListCells().getCell(cellIndex);
                    assertNotNull(cell, "ListCell index not found");

                    ContextMenu contextMenu = cell.getContextMenu();
                    if (!Objects.isNull(contextMenu)) {
                        contextMenu.hide();
                    }
                });
    }

    public void assertContextMenuShown(int cellIndex, String... expectedMenuItems)
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(
                () -> {
                    ListCell<?> cell = getListCells().getCell(cellIndex);
                    assertNotNull(cell, "ListCell index not found");

                    ContextMenu contextMenu = cell.getContextMenu();
                    assertNotNull(contextMenu, "ContextMenu not found");
                    assertTrue(contextMenu.isShowing(), "Context menu not showing");

                    if (expectedMenuItems.length > 0) {
                        Set<String> set = new HashSet<>(List.of(expectedMenuItems));
                        assertTrue(
                                contextMenu.getItems().stream()
                                        .map(MenuItem::getText)
                                        .anyMatch(s -> set.remove(s) && set.isEmpty()),
                                "ContextMenu does not contain expected menu items");
                    }
                });
    }

    public void assertEmpty() {
        assertThat(view.getWindow().lookup(query).queryListView().getItems())
                .describedAs("Listview \"%s\" on view \"%s\" is empty", query, view.getStageTitle())
                .isEmpty();
    }

    public void assertNotEmpty() {
        assertThat(view.getWindow().lookup(query).queryListView().getItems())
                .describedAs(
                        "Listview \"%s\" on view \"%s\" is not empty", query, view.getStageTitle())
                .isNotEmpty();
    }

    /**
     * Returns a {@link VirtualFlow} containing all {@link ListCell} within the {@link
     * javafx.scene.control.ListView}.
     */
    private VirtualFlow<ListCell<?>> getListCells() {
        javafx.scene.control.ListView<Object> listView =
                view.getWindow().lookup(query).queryListView();

        Optional<VirtualFlow> virtualFlowOptional =
                listView.getChildrenUnmodifiable().stream()
                        .filter(node -> node instanceof VirtualFlow)
                        .map(n -> (VirtualFlow) n)
                        .findFirst();

        return virtualFlowOptional.orElseThrow();
    }
}
