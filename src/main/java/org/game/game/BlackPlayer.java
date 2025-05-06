package org.game.game;

import org.pieces.pieces.BlackMan;
import org.pieces.pieces.Piece;

import java.util.List;

import static org.checkers.checkers.Main.BOARD_SIZE;

public class BlackPlayer extends Player {
    @Override
    public void initializePieces() {
        List<Piece> pieces = getPieces();
        pieces.clear();

        for (int row = 1; row <= 4; row++) {
            for (int col = 1; col <= BOARD_SIZE; col++) {
                if (row % 2 == 0 && col % 2 == 1 || row % 2 == 1 && col % 2 == 0) {
                    pieces.add(new BlackMan(row, col, this));
                }
            }
        }
    }
}
