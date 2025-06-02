package org.game.game;

import javafx.application.Platform;

public class GameTimer implements Runnable {
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Game game;


    public GameTimer(Game game, Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.game = game;
    }

    @Override
    public void run() {
        int PLAYER_TIME = 560;
        whitePlayer.remainingTime = PLAYER_TIME;
        blackPlayer.remainingTime = PLAYER_TIME;

        drawWhiteTime(whitePlayer.remainingTime);
        drawBlackTime(blackPlayer.remainingTime);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                Player activePlayer = game.getActivePlayer();
                if (activePlayer == whitePlayer) {
                    whitePlayer.remainingTime--;
                    drawWhiteTime(whitePlayer.remainingTime);
                } else if (activePlayer == blackPlayer) {
                    blackPlayer.remainingTime--;
                    drawBlackTime(blackPlayer.remainingTime);
                }
                if (blackPlayer.remainingTime == 0 || whitePlayer.remainingTime == 0) {
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
        String minutes = "" + timeLeft / 60;
        String seconds = ((timeLeft % 60) < 10 ? "0" : "") + timeLeft % 60;
        Platform.runLater(() -> game.gameSceneController.drawWhitePlayerTime(minutes + ":" + seconds));
    }

    void drawBlackTime(int timeLeft) {
        String minutes = "" + timeLeft / 60;
        String seconds = ((timeLeft % 60) < 10 ? "0" : "") + timeLeft % 60;
        Platform.runLater(() -> game.gameSceneController.drawBlackPlayerTime(minutes + ":" + seconds));
    }
}
