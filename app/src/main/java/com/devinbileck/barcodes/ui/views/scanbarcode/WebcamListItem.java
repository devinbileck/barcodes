package com.devinbileck.barcodes.ui.views.scanbarcode;

import java.util.Objects;

import com.github.sarxos.webcam.Webcam;

public record WebcamListItem(Webcam webcam) {
    @Override
    public String toString() {
        return webcam.getName();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof WebcamListItem)) {
            return false;
        }
        return Objects.equals(this.toString(), otherObject.toString());
    }
}
