package dev.taah.sharex;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import dev.taah.sharex.listener.GlobalKeyListener;
import dev.taah.sharex.manager.KeyManager;
import dev.taah.sharex.manager.ScreenshotManager;
import dev.taah.sharex.util.Constants;
import dev.taah.sharex.util.FileTransferable;
import dev.taah.sharex.util.KeyUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ScreenshotTool extends Application {


    private static KeyManager KEY_MANAGER;
    private static ScreenshotManager SCREENSHOT_MANAGER;
    private static Pane ROOT;
    private static Stage STAGE;

    @Override
    public void start(Stage stage) {
        ROOT = new Pane();
        Scene scene = new Scene(ROOT, 500, 1000);
        scene.setFill(Constants.OVERLAY_HIDDEN);

        ROOT.setOnMousePressed(event -> {
            if (!SCREENSHOT_MANAGER.screenshotting()) return;
            SCREENSHOT_MANAGER.overlay().setX(event.getX());
            SCREENSHOT_MANAGER.overlay().setY(event.getY());
            SCREENSHOT_MANAGER.overlay().setVisible(true);
            System.out.println("drag entered");
        });

        ROOT.setOnMouseDragged(event -> {
            if (!SCREENSHOT_MANAGER.screenshotting()) return;
            SCREENSHOT_MANAGER.overlay().setX(Math.min(event.getX(), SCREENSHOT_MANAGER.overlay().getX()));
            SCREENSHOT_MANAGER.overlay().setWidth(Math.abs(event.getX() - SCREENSHOT_MANAGER.overlay().getX()));
            SCREENSHOT_MANAGER.overlay().setY(Math.min(event.getY(), SCREENSHOT_MANAGER.overlay().getY()));
            SCREENSHOT_MANAGER.overlay().setHeight(Math.abs(event.getY() - SCREENSHOT_MANAGER.overlay().getY()));
        });

        ROOT.setOnMouseReleased(event -> {
            if (!SCREENSHOT_MANAGER.screenshotting()) return;
            int x = (int) SCREENSHOT_MANAGER.overlay().getX();
            int y = (int) SCREENSHOT_MANAGER.overlay().getY();
            int width = (int) SCREENSHOT_MANAGER.overlay().getWidth();
            int height = (int) SCREENSHOT_MANAGER.overlay().getHeight();
            SCREENSHOT_MANAGER.overlay().setStrokeWidth(0);
            SCREENSHOT_MANAGER.overlay().setVisible(false);
            SCREENSHOT_MANAGER.overlay().setWidth(0);
            SCREENSHOT_MANAGER.overlay().setHeight(0);
            stage.hide();
//            ROOT.setVisible(false);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> {
                        BufferedImage img;
                        try {
                            img = new Robot().createScreenCapture(new Rectangle(x, y, width, height));
                        } catch (AWTException e) {
                            throw new RuntimeException(e);
                        }
                        final File file = new File(new Date().toString() + ".png");
                        try {
                            ImageIO.write(img, "png", file);
                            FileTransferable ft = new FileTransferable(Collections.singletonList(file));
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ft, (clipboard, transferable) -> {
                                System.out.println("lost ownership");
                            });
                            Platform.setImplicitExit(false);
                            Platform.runLater(() -> {
                                SCREENSHOT_MANAGER.screenshotting(false);
                                stage.setFullScreen(false);
                                stage.setAlwaysOnTop(false);
                                stage.getScene().setFill(Constants.OVERLAY_HIDDEN);
                                stage.show();
                            });

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }, 250);
        });

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("dev.taah.sharex.ShareX");
        stage.setScene(scene);
        stage.show();
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        STAGE = stage;

        KEY_MANAGER = new KeyManager();
        System.out.println("Screenshot Keybind: " + KeyUtils.clickedKeysToString(KEY_MANAGER.screenshotKeybind()));
        SCREENSHOT_MANAGER = new ScreenshotManager();
        try {
            GlobalScreen.registerNativeHook();
            final GlobalKeyListener listener = new GlobalKeyListener();
            GlobalScreen.addNativeKeyListener(listener);
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

    public static Pane root() {
        return ROOT;
    }

    public static Stage stage() {
        return STAGE;
    }
}