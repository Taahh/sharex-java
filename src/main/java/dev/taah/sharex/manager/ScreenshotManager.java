package dev.taah.sharex.manager;

import dev.taah.sharex.ScreenshotTool;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class ScreenshotManager {
    private final Rectangle overlay;

    @Setter
    private boolean screenshotting;

    public ScreenshotManager() {
        overlay = new Rectangle();
        this.overlay.setFill(Color.TRANSPARENT);
        this.overlay.setStrokeType(StrokeType.INSIDE);
        this.overlay.setStroke(Color.WHITE);
        this.overlay.setStrokeWidth(5);
        ScreenshotTool.overlayScene().root().getChildren().add(this.overlay);
    }

    public Rectangle overlay() {
        this.overlay.setFill(Color.TRANSPARENT);
        this.overlay.setStrokeType(StrokeType.INSIDE);
        this.overlay.setStroke(Color.WHITE);
        this.overlay.setStrokeWidth(5);
        return this.overlay;
    }
}
