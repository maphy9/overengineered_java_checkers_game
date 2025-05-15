package org.checkers.checkers.elementControllers;

import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class MessageController {
    private final Label messageLabel;

    public MessageController(Label messageLabel) {
        this.messageLabel = messageLabel;
    }

    public void drawMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
    }
}
