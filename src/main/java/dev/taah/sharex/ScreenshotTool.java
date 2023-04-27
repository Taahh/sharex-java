package dev.taah.sharex;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import dev.taah.sharex.listener.OverlayKeyListener;
import dev.taah.sharex.manager.KeyManager;
import dev.taah.sharex.manager.ScreenshotManager;
import dev.taah.sharex.scene.MainScene;
import dev.taah.sharex.scene.OverlayScene;
import dev.taah.sharex.util.KeyUtils;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenshotTool extends Application {

    private static OverlayScene OVERLAY_SCENE;
    private static MainScene MAIN_SCENE;
    private static KeyManager KEY_MANAGER;
    private static ScreenshotManager SCREENSHOT_MANAGER;

    @Override
    public void start(Stage stage) {

        OVERLAY_SCENE = new OverlayScene();
        try {
            MAIN_SCENE = new MainScene();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MAIN_SCENE.stage().show();

        KEY_MANAGER = new KeyManager();
        System.out.println("Screenshot Keybind: " + KeyUtils.clickedKeysToString(KEY_MANAGER.screenshotKeybind()));
        SCREENSHOT_MANAGER = new ScreenshotManager();
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new OverlayKeyListener());
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyManager keyManager() {
        return KEY_MANAGER;
    }

    public static ScreenshotManager screenshotManager() {
        return SCREENSHOT_MANAGER;
    }

    public static OverlayScene overlayScene() {
        return OVERLAY_SCENE;
    }

    public static MainScene mainScene() {
        return MAIN_SCENE;
    }
}