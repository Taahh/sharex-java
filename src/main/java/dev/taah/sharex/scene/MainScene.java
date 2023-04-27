package dev.taah.sharex.scene;


import dev.taah.sharex.ScreenshotUIController;
import dev.taah.sharex.ShareX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class MainScene extends AbstractScene<Parent> {
    private final FXMLLoader loader;
    public MainScene() throws IOException {
        this.loader = new FXMLLoader(ShareX.class.getResource("main-interface.fxml"));
        this.root = loader.load();

        this.scene = new Scene(this.root());
        this.scene.setUserData(this.loader);

        this.stage.setTitle("ShareX");
        this.stage.setScene(this.scene);

        ScreenshotUIController controller = this.loader.getController();
        controller.imageHolder().getChildren().addAll(toImageView(Arrays.asList(new File("images").listFiles())));
    }

    private LinkedList<ImageView> toImageView(Collection<File> files) {
        LinkedList<ImageView> list = new LinkedList<>();
        files.forEach(file -> {
            try (FileInputStream fis = new FileInputStream(file)) {
                Image image = new Image(fis, 100, 100, false, false);
                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setImage(image);

                list.add(imageView);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return list;
    }
}
