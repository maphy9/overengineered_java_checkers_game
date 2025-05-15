package org.checkers.checkers.elementControllers;

import javafx.scene.control.Label;

public class WhitePlayerTimeController {
    private final Label whitePlayerTimeLeft;

    public WhitePlayerTimeController(Label whitePlayerTimeLeft) {
        this.whitePlayerTimeLeft = whitePlayerTimeLeft;
    }

    public void drawTime(String time) {
        whitePlayerTimeLeft.setText(time);
    }
}
