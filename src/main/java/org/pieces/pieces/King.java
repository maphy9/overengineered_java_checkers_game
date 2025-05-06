package org.pieces.pieces;

import javafx.scene.image.Image;
import org.game.game.AttackSequence;
import org.game.game.Player;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(int row, int col, Image pieceImage, Player player) {
        super(row, col, pieceImage, player);
    }

    @Override
    public boolean canMove(int newRow, int newCol) {
        return false;
    }

    @Override
    public List<AttackSequence> getLongestAttackSequences(char[][] board) {
        return new ArrayList<AttackSequence>();
    }

}
