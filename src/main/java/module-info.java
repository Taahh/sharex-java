module dev.taah.sharex {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.github.kwhat.jnativehook;
    requires lombok;

    opens dev.taah.sharex to javafx.fxml;
    exports dev.taah.sharex;
    exports dev.taah.sharex.manager;
}