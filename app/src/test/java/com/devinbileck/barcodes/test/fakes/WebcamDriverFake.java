package com.devinbileck.barcodes.test.fakes;

import com.github.sarxos.webcam.WebcamDevice;
import com.github.sarxos.webcam.WebcamDriver;

import java.util.Collections;
import java.util.List;

public class WebcamDriverFake implements WebcamDriver {
    @Override
    public List<WebcamDevice> getDevices() {
        return Collections.emptyList();
    }

    @Override
    public boolean isThreadSafe() {
        return true;
    }
}
