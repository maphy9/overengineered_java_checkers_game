package org.pieces.pieces;

import javafx.scene.image.Image;
import org.game.game.Player;

public class WhiteMan extends Man {
    public WhiteMan(int row, int col, Player player) {
        super(row, col, new Image(WhiteMan.class.getResourceAsStream("images/whiteMan.png")), player);
    }

    @Override
    public boolean canMove(int newRow, int newCol) {
        if (isPositionOccupied(newRow, newCol)) {
            return false;
        }

        if (!getOwner().getLongestAttackSequences().isEmpty()) {
            return canAttack(newRow, newCol);
        }

        int row = getRow();
        int col = getCol();

        if (newRow == row - 1 && newCol == col + 1) {
            return true;
        }

        if (newRow == row - 1 && newCol == col - 1) {
            return true;
        }

        return false;
    }
}
