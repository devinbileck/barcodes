package com.devinbileck.barcodes;

import javafx.application.Application;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.devinbileck.barcodes.ui.UiApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        Application.launch(UiApplication.class, args);
    }
}
