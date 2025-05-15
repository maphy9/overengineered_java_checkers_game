package org.game.game;

import javafx.application.Platform;

public class GameTimer implements Runnable {
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Game game;
    private final int PLAYER_TIME = 600;


    public GameTimer(Game game, Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.game = game;
    }

    @Override
    public void run() {
        int whitePlayerSecondsLeft = PLAYER_TIME;
        int blackPlayerSecondsLeft = PLAYER_TIME;

        drawWhiteTime(whitePlayerSecondsLeft);
        drawBlackTime(blackPlayerSecondsLeft);

        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(whitePlayer);
            System.out.println(blackPlayer);
            try {
                Thread.sleep(1000);
                Player activePlayer = game.getActivePlayer();
                if (activePlayer == whitePlayer) {
                    whitePlayerSecondsLeft--;
                    drawWhiteTime(whitePlayerSecondsLeft);
                } else if (activePlayer == blackPlayer) {
                    blackPlayerSecondsLeft--;
                    drawBlackTime(blackPlayerSecondsLeft);
                }
                if (blackPlayerSecondsLeft == 0 || whitePlayerSecondsLeft == 0) {
                    break;
                }
            } catch (InterruptedException e) {
                break;
            }
        }

        if (!game.gameSceneController.gameThread.isInterrupted()) {
            game.gameSceneController.gameThread.interrupt();
        }
    }

    void drawWhiteTime(int timeLeft) {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        Platform.runLater(() -> game.gameSceneController.drawWhiteTime(minutes + ":" + seconds));
    }

    void drawBlackTime(int timeLeft) {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        Platform.runLater(() -> game.gameSceneController.drawBlackTime(minutes + ":" + seconds));
    }
}
