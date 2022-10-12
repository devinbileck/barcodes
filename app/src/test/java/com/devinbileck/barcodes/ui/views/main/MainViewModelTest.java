package com.devinbileck.barcodes.ui.views.main;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainViewModelTest {
    private MainViewModel mainViewModel;

    @BeforeEach
    void setup() {
        mainViewModel = new MainViewModel();
    }

    @Test
    void uponInitialization_scannedContentListIsEmpty() {
        assertTrue(mainViewModel.getScannedContent().isEmpty());
    }

    @Test
    void addingNullContent_exceptionIsThrown() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> mainViewModel.addScannedContent(null));
    }

    @Test
    void addingScannedContent_contentIsPersisted() {
        List<String> contentToAdd = List.of("first content", "second content", "third content");

        contentToAdd.forEach(content -> mainViewModel.addScannedContent(content));

        List<String> scannedContent = mainViewModel.getScannedContent();
        assertThat(scannedContent).hasSameElementsAs(contentToAdd);
    }

    @Test
    void clearingScannedContent_contentIsCleared() {
        mainViewModel.addScannedContent("content");

        mainViewModel.clearScannedContent();

        assertThat(mainViewModel.getScannedContent()).isEmpty();
    }
}
