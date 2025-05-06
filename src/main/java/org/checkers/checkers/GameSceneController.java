package org.checkers.checkers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import org.checkers.checkers.elementControllers.GamePaneController;
import org.checkers.checkers.elementControllers.ScoresController;
import org.game.game.Game;
import org.game.game.Player;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable, SceneController {
    public GamePaneController gamePaneController;
    public ScoresController scoresController;
    private Thread gameThread;

    @FXML
    private Pane gamePane;

    @FXML
    private Label whitePlayerScoreLabel;

    @FXML
    private Label blackPlayerScoreLabel;

    public void switchToMainScene(ActionEvent event) throws Exception {
        if (gameThread != null) {
            gameThread.interrupt();
        }

        URL mainSceneFxml = getClass().getResource("scenes/main-scene.fxml");
        FXMLLoader loader = new FXMLLoader(mainSceneFxml);
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void cleanup() {
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (gameThread != null) {
            gameThread.interrupt();
        }

        Game game = new Game(this);

        scoresController = new ScoresController(whitePlayerScoreLabel, blackPlayerScoreLabel);

        gamePaneController = new GamePaneController(game, gamePane);
        gamePaneController.initialize();

        gameThread = new Thread(game);
        gameThread.start();
    }

    public void drawPieces(Player whitePlayer, Player blackPlayer) {
        gamePaneController.drawPieces(whitePlayer, blackPlayer);
    }

    public void drawScores(int whitePlayerScore, int blackPlayerScore) {
        scoresController.drawScores(whitePlayerScore, blackPlayerScore);
    }
}
