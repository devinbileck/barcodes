package com.devinbileck.barcodes.test.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;

/** Utility methods for working with JavaFX threads. */
public final class JavaFxThreadUtil {
    private static final long DEFAULT_TIMEOUT_MILLIS = 5000L;

    /**
     * This method is used to execute code on the JavaFX thread. It is intended to be used in
     * testing scenarios only because the internal exception handling is optimized for testing
     * purposes.
     *
     * <p>This method is blocking. It will execute the given runnable and wait for it to be
     * finished. If the runnable does not complete within {@value DEFAULT_TIMEOUT_MILLIS}
     * milliseconds, a {@link TimeoutException} will be thrown. If you need to specify the timeout
     * value, use {@link #runInFxThread(Runnable, long)}.
     *
     * <p>This method will catch exceptions produced by executing the runnable. If an {@link
     * AssertionError} is thrown by the runnable, for example because a test assertion has failed,
     * the assertion error will be rethrown by this method so that the JUnit test runner will
     * properly handle it and show the test failure.
     *
     * @param code the code to execute on the JavaFX thread
     */
    public static void runInFxThread(final Runnable code)
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(code, DEFAULT_TIMEOUT_MILLIS);
    }

    /**
     * Similar to {@link #runInFxThread(Runnable)}, but takes a timeout parameter.
     *
     * @param code the code to execute on the JavaFX thread
     * @param timeout the maximum time to wait in milliseconds before execution is aborted
     */
    public static void runInFxThread(final Runnable code, final long timeout)
            throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Object> future = new CompletableFuture<>();

        Platform.runLater(
                () -> {
                    try {
                        code.run();
                    } catch (AssertionError e) {
                        future.complete(e);
                        return;
                    }
                    future.complete(null);
                });

        Object result = future.get(timeout, TimeUnit.MILLISECONDS);

        if (result instanceof AssertionError assertionError) {
            throw assertionError;
        }
    }

    /**
     * This method is used to wait until the UI thread has done all work that was queued via {@link
     * Platform#runLater(Runnable)}.
     *
     * <p>If the UI thread does not complete within {@value DEFAULT_TIMEOUT_MILLIS} milliseconds, a
     * {@link TimeoutException} will be thrown. If you need to specify the timeout value, use {@link
     * #waitForUiThread(long)}.
     */
    public static void waitForUiThread()
            throws ExecutionException, InterruptedException, TimeoutException {
        waitForUiThread(DEFAULT_TIMEOUT_MILLIS);
    }

    /**
     * Similar to {@link #waitForUiThread()}, but takes a timeout parameter.
     *
     * @param timeout the maximum time to wait in milliseconds
     */
    public static void waitForUiThread(final long timeout)
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(() -> {}, timeout);
    }
}
