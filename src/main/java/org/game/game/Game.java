package org.game.game;

import javafx.application.Platform;
import org.checkers.checkers.GameSceneController;
import org.pieces.pieces.Piece;

import java.util.Arrays;
import java.util.List;

import static org.checkers.checkers.Main.BOARD_SIZE;

public class Game implements Runnable {
    public final GameSceneController gameSceneController;
    public Player whitePlayer;
    public Player blackPlayer;
    private Player activePlayer;
    Thread gameTimerThread;

    public Game(GameSceneController gameSceneController, Player whitePlayer, Player blackPlayer) {
        this.gameSceneController = gameSceneController;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    @Override
    public void run() {
        configureGame();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                activePlayer = whitePlayer;
                whitePlayer.turn();
                blackPlayer.setLongestAttackSequences(Game.getBoard(blackPlayer, whitePlayer));
                drawScene();
                if (isGameOver()) {
                    drawMessage("White player wins");
                    break;
                }
                if (!blackPlayer.canMakeAnyMove()) {
                    drawMessage("White player wins");
                    break;
                }

                activePlayer = blackPlayer;
                blackPlayer.turn();
                whitePlayer.setLongestAttackSequences(Game.getBoard(whitePlayer, blackPlayer));
                drawScene();
                if (isGameOver()) {
                    drawMessage("Black player wins");
                    break;
                }
                if (!whitePlayer.canMakeAnyMove()) {
                    drawMessage("Black player wins");
                    break;
                }
            } catch(Exception e) {
                break;
            }
        }

        if (!gameTimerThread.isInterrupted()) {
            gameTimerThread.interrupt();
        }
    }

    private void configureGame() {
        whitePlayer.setEnemy(blackPlayer);
        blackPlayer.setEnemy(whitePlayer);

        drawScene();

        GameTimer gameTimer = new GameTimer(this, whitePlayer, blackPlayer);
        gameTimerThread = new Thread(gameTimer);
        gameTimerThread.start();
    }

    private void drawScene() {
        Platform.runLater(gameSceneController::drawScene);
    }

    private void drawMessage(String message) {
        Platform.runLater(() -> gameSceneController.drawMessage(message));
    }

    private boolean isGameOver() {
        if (whitePlayer.getPieces().isEmpty()) {
            return true;
        }
        return blackPlayer.getPieces().isEmpty();
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
