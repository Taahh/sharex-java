package dev.taah.sharex.scene;


import dev.taah.sharex.ShareX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class MainScene extends AbstractScene<Parent> {
    public MainScene() throws IOException {
        super(FXMLLoader.load(Objects.requireNonNull(ShareX.class.getResource("main-interface.fxml"))));
        this.scene = new Scene(this.root());
        this.stage.setTitle("ShareX");
        this.stage.setScene(this.scene);
    }
}
