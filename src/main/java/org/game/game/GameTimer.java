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
        int PLAYER_TIME = 600;
        whitePlayer.remainingTime.set(PLAYER_TIME);
        blackPlayer.remainingTime.set(PLAYER_TIME);

        drawWhiteTime(whitePlayer.remainingTime.get());
        drawBlackTime(blackPlayer.remainingTime.get());

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1000);
                Player activePlayer = game.getActivePlayer();
                if (activePlayer == whitePlayer) {
                    whitePlayer.remainingTime.set(whitePlayer.remainingTime.get() - 1);
                } else if (activePlayer == blackPlayer) {
                    blackPlayer.remainingTime.set(blackPlayer.remainingTime.get() - 1);
                }
                drawWhiteTime(whitePlayer.remainingTime.get());
                drawBlackTime(blackPlayer.remainingTime.get());
                if (blackPlayer.remainingTime.get() == 0 || whitePlayer.remainingTime.get() == 0) {
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
