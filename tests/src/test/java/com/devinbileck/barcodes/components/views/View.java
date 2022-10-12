package com.devinbileck.barcodes.components.views;

import static org.testfx.api.FxAssert.verifyThat;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.api.FxRobot;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.util.WaitForAsyncUtils;

public abstract class View {
    private final FxRobot robot = new FxRobot();

    public FxRobot getWindow() {
        return robot.targetWindow(getStageTitle());
    }

    public <T> void interact(Callable<T> callable) {
        robot.interact(callable);
    }

    public boolean isShowing() {
        try {
            return robot.window(getStageTitle()).isShowing();
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public void waitUntilShowing(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, this::isShowing);
    }

    public void waitUntilNotShowing(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, () -> !isShowing());
    }

    public void assertIsShowing() {
        verifyThat(robot.window(getStageTitle()), WindowMatchers.isShowing());
    }

    public void assertIsNotShowing() {
        try {
            verifyThat(robot.window(getStageTitle()), WindowMatchers.isNotShowing());
        } catch (NoSuchElementException ignored) {
            // Do nothing, it is not showing
        }
    }

    public abstract String getStageTitle();
}
