package org.checkers.checkers.elementControllers;

import javafx.scene.control.Label;

public class BlackPlayerTimeController {
    private final Label blackPlayerTimeLeft;

    public BlackPlayerTimeController(Label blackPlayerTimeLeft) {
        this.blackPlayerTimeLeft = blackPlayerTimeLeft;
    }

    public void drawTime(String time) {
        blackPlayerTimeLeft.setText(time);
    }
}
