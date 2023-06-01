module com.example.babel_char {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.babel_chat_server to javafx.fxml;
    exports com.example.babel_chat_server;
}