package org.checkers.checkers;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import org.game.game.*;
import org.pieces.pieces.Piece;

import java.net.URL;
import java.util.ResourceBundle;

public class LocalGameSceneController extends GameSceneController {
    @Override
    public void cleanup() {
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Player whitePlayer = new LocalPlayer();
        Player.initializeWhitePieces(whitePlayer);
        Player blackPlayer = new LocalPlayer();
        Player.initializeBlackPieces(blackPlayer);

        game = new Game(this, whitePlayer, blackPlayer);

        gameThread = new Thread(game);
        gameThread.start();
    }
}
