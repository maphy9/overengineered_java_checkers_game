package org.game.game;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.checkers.checkers.GameSceneController;
import org.pieces.pieces.Piece;

import java.util.Arrays;
import java.util.List;

import static org.checkers.checkers.Main.BOARD_SIZE;

public class Game implements Runnable {
    public final GameSceneController gameSceneController;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player activePlayer;
    private final Label whitePlayerTimeLeft;
    private final Label blackPlayerTimeLeft;

    public Game(GameSceneController gameSceneController, Label whitePlayerTimeLeft, Label blackPlayerTimeLeft) {
        this.gameSceneController = gameSceneController;
        this.whitePlayerTimeLeft = whitePlayerTimeLeft;
        this.blackPlayerTimeLeft = blackPlayerTimeLeft;
    }

    @Override
    public void run() {
        configureGame();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                activePlayer = whitePlayer;
                whitePlayer.turn();
                drawScores();
                drawPieces();
                if (isGameOver()) {
                    drawMessage();
                    break;
                }

                activePlayer = blackPlayer;
                blackPlayer.turn();
                drawScores();
                drawPieces();
                if (isGameOver()) {
                    drawMessage();
                    break;
                }
            } catch(Exception e) {
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

        GameTimer gameTimer = new GameTimer(this, whitePlayer, blackPlayer);
        Thread gameTimerThread = new Thread(gameTimer);
        gameTimerThread.start();
    }

    private void drawPieces() {
        Platform.runLater(() -> gameSceneController.drawPieces(whitePlayer, blackPlayer));
    }

    private void drawScores() {
        Platform.runLater(() -> gameSceneController.drawScores(whitePlayer.getScore(), blackPlayer.getScore()));
    }

    private void drawMessage() {
        String message = "";
        if (whitePlayer.getPieces().isEmpty()) {
            message = "Black player wins!";
        }
        if (blackPlayer.getPieces().isEmpty()) {
            message = "White player wins!";
        }
        String finalMessage = message;
        Platform.runLater(() -> gameSceneController.drawMessage(finalMessage));
    }

    private boolean isGameOver() {
        if (whitePlayer.getPieces().isEmpty()) {
            return true;
        }
        if (blackPlayer.getPieces().isEmpty()) {
            return true;
        }
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
