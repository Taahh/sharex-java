package dev.taah.sharex;

import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class ScreenshotUIController {
    @FXML
    private TilePane imageHolder;
}
