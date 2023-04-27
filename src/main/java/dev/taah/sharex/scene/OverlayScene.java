package dev.taah.sharex.scene;

import dev.taah.sharex.ScreenshotTool;
import dev.taah.sharex.util.Constants;
import dev.taah.sharex.util.FileTransferable;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Accessors(fluent = true)
@Getter
public class OverlayScene extends AbstractScene<Pane> {
    public OverlayScene() {
        super(new Pane());
        this.scene = new Scene(this.root, 500, 1000);
        this.scene.setFill(Constants.OVERLAY_COLOR);

        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setScene(this.scene);
        this.stage.setFullScreen(true);
        this.stage.setAlwaysOnTop(true);
        this.stage.setFullScreenExitHint("");
        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        this.root.setOnMousePressed(event -> {
            if (!ScreenshotTool.screenshotManager().screenshotting()) return;
            ScreenshotTool.screenshotManager().overlay().setX(event.getX());
            ScreenshotTool.screenshotManager().overlay().setY(event.getY());
            ScreenshotTool.screenshotManager().overlay().setVisible(true);
            System.out.println("drag entered");
        });

        this.root.setOnMouseDragged(event -> {
            if (!ScreenshotTool.screenshotManager().screenshotting()) return;
            ScreenshotTool.screenshotManager().overlay().setX(Math.min(event.getX(), ScreenshotTool.screenshotManager().overlay().getX()));
            ScreenshotTool.screenshotManager().overlay().setWidth(Math.abs(event.getX() - ScreenshotTool.screenshotManager().overlay().getX()));
            ScreenshotTool.screenshotManager().overlay().setY(Math.min(event.getY(), ScreenshotTool.screenshotManager().overlay().getY()));
            ScreenshotTool.screenshotManager().overlay().setHeight(Math.abs(event.getY() - ScreenshotTool.screenshotManager().overlay().getY()));
        });

        this.root.setOnMouseReleased(event -> {
            if (!ScreenshotTool.screenshotManager().screenshotting()) return;
            int x = (int) ScreenshotTool.screenshotManager().overlay().getX();
            int y = (int) ScreenshotTool.screenshotManager().overlay().getY();
            int width = (int) ScreenshotTool.screenshotManager().overlay().getWidth();
            int height = (int) ScreenshotTool.screenshotManager().overlay().getHeight();
            ScreenshotTool.screenshotManager().overlay().setStrokeWidth(0);
            ScreenshotTool.screenshotManager().overlay().setVisible(false);
            ScreenshotTool.screenshotManager().overlay().setWidth(0);
            ScreenshotTool.screenshotManager().overlay().setHeight(0);
            this.stage.hide();
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
                                ScreenshotTool.screenshotManager().screenshotting(false);
                                ScreenshotTool.mainScene().stage.show();
                            });

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }, 250);
        });
    }
}
