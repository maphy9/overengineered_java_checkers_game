package org.checkers.checkers.elementControllers;

import javafx.scene.control.Label;

public class ScoresController {
    private final Label whitePlayerScoreLabel;
    private final Label blackPlayerScoreLabel;

    public ScoresController(Label whitePlayerScoreLabel, Label blackPlayerScoreLabel) {
        this.whitePlayerScoreLabel = whitePlayerScoreLabel;
        this.blackPlayerScoreLabel = blackPlayerScoreLabel;
    }

    public void drawScores(int whitePlayerScore, int blackPlayerScore) {
        whitePlayerScoreLabel.setText(String.valueOf(whitePlayerScore));
        blackPlayerScoreLabel.setText(String.valueOf(blackPlayerScore));
    }
}
