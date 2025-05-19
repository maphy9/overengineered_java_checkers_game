package org.checkers.checkers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class NetworkJoinSceneController implements Initializable, Cleanable {
    @FXML
    private Label myIPAddress;

    public void switchToMainScene(ActionEvent event) throws Exception {
        cleanup();
        URL mainSceneFxml = getClass().getResource("scenes/main-scene.fxml");
        FXMLLoader loader = new FXMLLoader(mainSceneFxml);
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setMyIPAddress() {
        String text = "";
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            text = localhost.getHostAddress();
        } catch (Exception e) {
            text = "Error";
        }
        myIPAddress.setText(text);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMyIPAddress();
    }
}
