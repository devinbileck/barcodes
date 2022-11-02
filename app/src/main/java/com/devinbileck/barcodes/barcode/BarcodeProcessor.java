package com.devinbileck.barcodes.barcode;

import java.awt.image.BufferedImage;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class BarcodeProcessor {
    private BarcodeProcessor() {
    }

    public static Optional<BarcodeResult> process(final @NotNull BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        BarcodeResult result;
        try {
            Result r = new MultiFormatReader().decode(bitmap);
            result = new BarcodeResult(r.getBarcodeFormat(), r.getText(), image);
        } catch (NotFoundException ignored) {
            // There is no barcode in the image
            return Optional.empty();
        }
        return Optional.of(result);
    }
}
