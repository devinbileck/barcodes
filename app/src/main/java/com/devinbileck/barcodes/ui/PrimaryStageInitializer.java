package com.devinbileck.barcodes.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.devinbileck.barcodes.ui.views.main.MainViewController;

import net.rgielen.fxweaver.core.FxWeaver;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {
    private final FxWeaver fxWeaver;

    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        Scene scene = new Scene(fxWeaver.loadView(MainViewController.class));
        stage.setScene(scene);
        stage.setTitle("Barcodes");
        stage.centerOnScreen();
        stage.show();

        // Must set the minimum width and height of the stage after showing it,
        // otherwise it will be ignored
        stage.setMinWidth(400);
        stage.setMinHeight(400);
    }
}
