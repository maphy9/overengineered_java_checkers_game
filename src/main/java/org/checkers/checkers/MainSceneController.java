package org.checkers.checkers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainSceneController implements SceneController {
    public void switchToGameScene(ActionEvent event) throws Exception {
        URL gameSceneFxml = getClass().getResource("scenes/game-scene.fxml");
        FXMLLoader loader = new FXMLLoader(gameSceneFxml);
        Parent root = loader.load();
        SceneController controller = loader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            if (controller != null) {
                controller.cleanup();
            }
        });

        stage.show();
    }

    @Override
    public void cleanup() {

    }
}
