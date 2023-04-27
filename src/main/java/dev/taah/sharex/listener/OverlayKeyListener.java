package dev.taah.sharex.listener;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import dev.taah.sharex.ScreenshotTool;
import dev.taah.sharex.manager.KeyManager;
import dev.taah.sharex.util.Constants;
import dev.taah.sharex.util.KeyUtils;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.SortedSet;
import java.util.TreeSet;

public class OverlayKeyListener implements NativeKeyListener, NativeMouseListener {

    private static final SortedSet<KeyManager.ClickedKey> PRESSED_KEYS = new TreeSet<>();

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {

//        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()) + " : " + nativeEvent.getKeyCode() + " : " + nativeEvent.getKeyLocation());
        PRESSED_KEYS.add(new KeyManager.ClickedKey(nativeEvent.getKeyCode(), nativeEvent.getKeyLocation()));
//        System.out.println("Currently pressed: " + KeyUtils.clickedKeysToString(PRESSED_KEYS));
        if (PRESSED_KEYS.size() == ScreenshotTool.keyManager().screenshotKeybind().size()) {
            boolean screenshotting = true;

            for (KeyManager.ClickedKey pressedKey : PRESSED_KEYS) {
                if (!ScreenshotTool.keyManager().screenshotKeybind().contains(pressedKey)) {
                    screenshotting = false;
                }
            }

            if (screenshotting) {
                boolean previous = ScreenshotTool.screenshotManager().screenshotting();
                if (!previous) {
                    Platform.setImplicitExit(false);
                    Platform.runLater(() -> {
                        ScreenshotTool.mainScene().stage().hide();
                        ScreenshotTool.overlayScene().stage().show();

                    });
                    ScreenshotTool.screenshotManager().screenshotting(true);
                }
            }
        } else {
            if (ScreenshotTool.screenshotManager().screenshotting() && nativeEvent.getKeyCode() == ScreenshotTool.keyManager().cancelKeybind()) {
                Platform.setImplicitExit(false);
                Platform.runLater(() -> {
                    ScreenshotTool.overlayScene().stage().hide();
                    ScreenshotTool.mainScene().stage().show();
                });
                ScreenshotTool.screenshotManager().screenshotting(false);
            }
        }
    }


    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        PRESSED_KEYS.removeIf(clickedKey -> clickedKey.location() == nativeEvent.getKeyLocation() && clickedKey.keyCode() == nativeEvent.getKeyCode());
    }
}
