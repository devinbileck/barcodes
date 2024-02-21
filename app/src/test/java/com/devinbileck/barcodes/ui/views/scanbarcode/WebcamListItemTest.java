package com.devinbileck.barcodes.ui.views.scanbarcode;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;

import com.devinbileck.barcodes.webcam.DisconnectedWebcamDeviceFake;
import com.devinbileck.barcodes.webcam.NonOpeningWebcamDeviceFake;
import com.devinbileck.barcodes.webcam.WebcamFake;

class WebcamListItemTest {
    private final WebcamFake webcam = new WebcamFake(new DisconnectedWebcamDeviceFake());

    @Test
    void testToStringFormat() {
        WebcamListItem webcamListItem = new WebcamListItem(webcam);
        assertThat(webcamListItem).hasToString("DisconnectedWebcamDeviceFake");
    }

    @Test
    void testEqualsWithEqualObjects() {
        WebcamListItem webcamListItem = new WebcamListItem(webcam);
        WebcamListItem otherObject = new WebcamListItem(webcam);
        assertThat(webcamListItem.equals(otherObject)).isTrue();
    }

    @Test
    void testEqualsWithDifferentObjectType() {
        WebcamListItem webcamListItem = new WebcamListItem(webcam);
        assertThat(webcamListItem.equals(new Object())).isFalse();
    }

    @Test
    void testEqualsWithNonEqualObjects() {
        Webcam otherWebcam = new WebcamFake(
                new NonOpeningWebcamDeviceFake(
                        new WebcamException("Webcam exception when opening")));
        WebcamListItem webcamListItem = new WebcamListItem(webcam);
        WebcamListItem otherObject = new WebcamListItem(otherWebcam);
        assertThat(webcamListItem.equals(otherObject)).isFalse();
    }
}
