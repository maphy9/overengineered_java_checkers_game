package org.checkers.checkers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class Main extends Application {
    public static final int BOARD_SIZE = 10;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL mainSceneFxml = getClass().getResource("scenes/main-scene.fxml");
        Parent root = FXMLLoader.load(mainSceneFxml);
        Scene scene = new Scene(root);

        stage.setTitle("Checkers");
        InputStream iconUrl = getClass().getResourceAsStream("images/icon.png");
        Image icon = new Image(iconUrl);
        stage.getIcons().add(icon);
        stage.setResizable(false);

        stage.setScene(scene);

        stage.show();
    }
}