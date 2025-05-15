package org.pieces.pieces;

import javafx.scene.image.Image;
import org.game.game.Player;

public class WhiteKing extends King {
    public WhiteKing(int row, int col, Player player) {
        super(row, col, new Image(BlackMan.class.getResourceAsStream("images/whiteKing.png")), player);
    }
}
