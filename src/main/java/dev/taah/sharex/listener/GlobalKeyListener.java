package dev.taah.sharex.listener;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import dev.taah.sharex.ScreenshotTool;
import dev.taah.sharex.manager.KeyManager;
import dev.taah.sharex.util.Constants;
import dev.taah.sharex.util.KeyUtils;
import javafx.application.Platform;

import java.util.SortedSet;
import java.util.TreeSet;

public class GlobalKeyListener implements NativeKeyListener, NativeMouseListener {

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
//                System.out.println("Screenshotting");
                boolean previous = ScreenshotTool.screenshotManager().screenshotting();
                if (!previous) {
//                    System.out.println("not previously screenshotting");
                    //show overlay and start drag
                    Platform.setImplicitExit(false);
                    Platform.runLater(() -> {
//                        System.out.println("showing stage");
                        ScreenshotTool.stage().getScene().setFill(Constants.OVERLAY_COLOR);
                        ScreenshotTool.stage().setFullScreen(true);
                        ScreenshotTool.stage().setAlwaysOnTop(true);
//                        System.out.println("finished");
                    });
                    ScreenshotTool.screenshotManager().screenshotting(true);
                }
            }
        } else {
            if (ScreenshotTool.screenshotManager().screenshotting() && nativeEvent.getKeyCode() == ScreenshotTool.keyManager().cancelKeybind()) {
                Platform.setImplicitExit(false);
                Platform.runLater(() -> {
                    ScreenshotTool.stage().setFullScreen(false);
                    ScreenshotTool.stage().show();
                    ScreenshotTool.stage().setAlwaysOnTop(false);
                    ScreenshotTool.stage().getScene().setFill(Constants.OVERLAY_HIDDEN);
                    ScreenshotTool.screenshotManager().overlay().setVisible(false);
                    ScreenshotTool.screenshotManager().overlay().setWidth(0);
                    ScreenshotTool.screenshotManager().overlay().setHeight(0);
                });
                ScreenshotTool.screenshotManager().screenshotting(false);
//                System.out.println("not screenshotting");
            }
        }
    }



    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
        PRESSED_KEYS.removeIf(clickedKey -> clickedKey.location() == nativeEvent.getKeyLocation() && clickedKey.keyCode() == nativeEvent.getKeyCode());
    }
}
