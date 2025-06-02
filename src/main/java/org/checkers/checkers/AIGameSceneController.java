package org.checkers.checkers;

import org.game.game.AIPlayer;
import org.game.game.Game;
import org.game.game.LocalPlayer;
import org.game.game.Player;

import java.net.URL;
import java.util.ResourceBundle;

public class AIGameSceneController extends GameSceneController {
    @Override
    public void cleanup() {
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int randomNumber = (int) (Math.random() * 2);
        Player whitePlayer, blackPlayer;
        if (randomNumber == 0) {
            whitePlayer = new LocalPlayer();
            Player.initializeWhitePieces(whitePlayer);
            blackPlayer = new AIPlayer();
            Player.initializeBlackPieces(blackPlayer);
        } else {
            whitePlayer = new AIPlayer();
            Player.initializeWhitePieces(whitePlayer);
            blackPlayer = new LocalPlayer();
            Player.initializeBlackPieces(blackPlayer);
        }


        game = new Game(this, whitePlayer, blackPlayer);

        gameThread = new Thread(game);
        gameThread.start();
    }
}

