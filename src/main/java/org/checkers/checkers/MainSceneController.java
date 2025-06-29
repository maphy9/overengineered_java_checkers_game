package org.checkers.checkers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainSceneController {
    public void switchToLocalGameScene(ActionEvent event) throws Exception {
        URL localGameSceneFxml = getClass().getResource("scenes/local-game-scene.fxml");
        FXMLLoader localGameLoader = new FXMLLoader(localGameSceneFxml);
        Parent root = localGameLoader.load();
        GameSceneController localGameController = localGameLoader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            if (localGameController != null) {
                localGameController.cleanup();
            }
        });

        stage.show();
    }

    public void switchToAIGameScene(ActionEvent event) throws Exception {
        URL localGameSceneFxml = getClass().getResource("scenes/ai-game-scene.fxml");
        FXMLLoader localGameLoader = new FXMLLoader(localGameSceneFxml);
        Parent root = localGameLoader.load();
        GameSceneController localGameController = localGameLoader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            if (localGameController != null) {
                localGameController.cleanup();
            }
        });

        stage.show();
    }

    public void switchToNetworkJoinScene(ActionEvent event) throws Exception {
        URL networkJoinSceneFxml = getClass().getResource("scenes/network-join-scene.fxml");
        FXMLLoader networkJoinSceneLoader = new FXMLLoader(networkJoinSceneFxml);
        Parent root = networkJoinSceneLoader.load();
        NetworkJoinSceneController networkJoinSceneController = networkJoinSceneLoader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            networkJoinSceneController.cleanup();
        });

        stage.show();
    }
}
