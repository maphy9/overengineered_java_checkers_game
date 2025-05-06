package org.game.game;

import javafx.application.Platform;
import org.checkers.checkers.GameSceneController;
import org.pieces.pieces.Piece;

import java.util.Arrays;
import java.util.List;

import static org.checkers.checkers.Main.BOARD_SIZE;

public class Game implements Runnable {
    private final GameSceneController gameSceneController;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player activePlayer;

    public Game(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    @Override
    public void run() {
        configureGame();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                activePlayer = whitePlayer;
                whitePlayer.turn();
                drawPieces();
                if (isGameOver()) {
                    break;
                }

                activePlayer = blackPlayer;
                blackPlayer.turn();
                drawPieces();
                if (isGameOver()) {
                    break;
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void configureGame() {
        whitePlayer = new WhitePlayer();
        whitePlayer.initializePieces();

        blackPlayer = new BlackPlayer();
        blackPlayer.initializePieces();

        whitePlayer.setEnemy(blackPlayer);
        blackPlayer.setEnemy(whitePlayer);

        drawPieces();
    }

    private void drawPieces() {
        Platform.runLater(() -> gameSceneController.gamePaneController.drawPieces(whitePlayer, blackPlayer));
    }

    private boolean isGameOver() {
        return false;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public static char[][] getBoard(Player player, Player enemy) {
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for (char[] row : board) {
            Arrays.fill(row, ' ');
        }

        List<Piece> playerPieces = player.getPieces();
        for (Piece piece : playerPieces) {
            int row = piece.getRow() - 1;
            int col = piece.getCol() - 1;
            board[row][col] = 'P';
        }

        List<Piece> enemyPieces = enemy.getPieces();
        for (Piece piece : enemyPieces) {
            int row = piece.getRow() - 1;
            int col = piece.getCol() - 1;
            board[row][col] = 'E';
        }

        return board;
    }
}
