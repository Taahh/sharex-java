package dev.taah.sharex.scene;


import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public abstract class AbstractScene<T> {
    protected final T root;
    protected Stage stage;
    protected Scene scene;

    public AbstractScene(T root) {
        this.root = root;
        this.stage = new Stage();
    }
}
