package com.devinbileck.barcodes.barcode;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class BarcodeProcessor {
    private BarcodeProcessor() {
    }

    public static Optional<BarcodeResult> process(final @NotNull BufferedImage image) {
        final LuminanceSource source = new BufferedImageLuminanceSource(image);
        final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        final Map<DecodeHintType, Object> hints = Map.of(
                DecodeHintType.PURE_BARCODE, Boolean.FALSE
        );

        final BarcodeResult result;
        try {
            final Result r = new MultiFormatReader().decode(bitmap, hints);
            result = new BarcodeResult(r.getBarcodeFormat(), r.getText(), image);
        } catch (NotFoundException ignored) {
            // There is no barcode in the image
            return Optional.empty();
        }
        return Optional.of(result);
    }
}
