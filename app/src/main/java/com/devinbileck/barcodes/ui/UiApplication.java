package com.devinbileck.barcodes.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.devinbileck.barcodes.MainApplication;
import com.devinbileck.barcodes.image.IconFactory;

public class UiApplication extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        //noinspection ZeroLengthArrayAllocation
        this.context =
                new SpringApplicationBuilder()
                        .sources(MainApplication.class)
                        .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(IconFactory.getImage(IconFactory.ICON.APP));
        context.publishEvent(new com.devinbileck.barcodes.ui.StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }
}
