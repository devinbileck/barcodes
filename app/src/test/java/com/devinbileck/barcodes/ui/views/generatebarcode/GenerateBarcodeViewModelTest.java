package com.devinbileck.barcodes.ui.views.generatebarcode;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.devinbileck.barcodes.barcode.BarcodeResult;
import com.devinbileck.barcodes.image.ImageUtil;
import com.google.zxing.BarcodeFormat;

import javafx.embed.swing.SwingFXUtils;

class GenerateBarcodeViewModelTest {
    private final GenerateBarcodeViewModel viewModel = new GenerateBarcodeViewModel();

    @Test
    void whenInitializingViewModel_thenAppropriateDefaultsAreSet() {
        assertThat(viewModel.getSpecifiedContent().get()).isEmpty();
        assertThat(viewModel.getSpecifiedFormat().get()).isNull();
        assertThat(viewModel.getAvailableBarcodeFormats()).hasSize(17);
        assertThat(viewModel.canGenerate().get()).isFalse();
        assertThat(viewModel.getGeneratedResult().get()).isEmpty();
    }

    @Test
    void whenSettingGeneratedResult_thenResultIsPersisted() {
        final BufferedImage bufferedImage = new BufferedImage(640, 480, TYPE_INT_RGB);
        viewModel.setGeneratedResult(Optional.of(new BarcodeResult(BarcodeFormat.QR_CODE, "test", bufferedImage)));

        BarcodeResult generatedResult = viewModel.getGeneratedResult().get().orElseThrow();

        assertThat(generatedResult)
                .extracting(BarcodeResult::format, BarcodeResult::text)
                .containsExactly(BarcodeFormat.QR_CODE, "test");

        assertThat(ImageUtil.isImagesIdentical(bufferedImage, generatedResult.image())).isTrue();
    }

    @Test
    void whenNecessaryFieldsAreSet_thenCanGenerate() {
        viewModel.setSpecifiedContent("test");
        viewModel.setSpecifiedFormat(new BarcodeFormatListItem(BarcodeFormat.QR_CODE));
        assertThat(viewModel.canGenerate().get()).isTrue();
    }
}
