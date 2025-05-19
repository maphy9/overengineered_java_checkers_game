package org.game.game;

import org.pieces.pieces.Piece;
import org.pieces.pieces.WhiteMan;

import java.util.List;

import static org.checkers.checkers.Main.BOARD_SIZE;

public abstract class WhitePlayer extends Player {
    @Override
    public void initializePieces() {
        List<Piece> pieces = getPieces();
        pieces.clear();

        for (int row = BOARD_SIZE; row >= 7; row--) {
            for (int col = 1; col <= BOARD_SIZE; col++) {
                if (row % 2 == 0 && col % 2 == 1 || row % 2 == 1 && col % 2 == 0) {
                    pieces.add(new WhiteMan(row, col, this));
                }
            }
        }
    }
}
