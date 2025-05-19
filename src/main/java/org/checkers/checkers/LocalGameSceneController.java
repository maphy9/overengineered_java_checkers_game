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
        WhitePlayer whitePlayer = new LocalWhitePlayer();
        BlackPlayer blackPlayer = new LocalBlackPlayer();
        game = new Game(this, whitePlayer, blackPlayer);

        gameThread = new Thread(game);
        gameThread.start();
    }

    @Override
    public void handleSquareClick(Rectangle square) {
        Player activePlayer = game.getActivePlayer();
        if (activePlayer == null) {
            return;
        }
        if (activePlayer.getSelectedPiece() == null) {
            return;
        }
        int[] coordinates = (int[]) square.getUserData();
        activePlayer.setSelectedRow(coordinates[0]);
        activePlayer.setSelectedCol(coordinates[1]);
    }

    @Override
    public void handlePieceClick(Piece piece, Player player, Group piecesGroup) {
        if (player != game.getActivePlayer()) {
            return;
        }
        if (piece == player.getSelectedPiece()) {
            player.setSelectedPiece(null);
        } else {
            player.setSelectedPiece(piece);
        }
        player.setSelectedRow(-1);
        player.setSelectedRow(-1);
        drawScene();
    }
}
