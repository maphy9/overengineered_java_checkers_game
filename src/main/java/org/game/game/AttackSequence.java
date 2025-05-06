package org.game.game;

import org.pieces.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class AttackSequence {
    public final Piece piece;
    public final List<int[]> capturedPositions = new ArrayList<>();
    public int sourceRow;
    public int sourceCol;
    public int targetRow;
    public int targetCol;
    public int length;

    public AttackSequence(Piece piece) {
        this.piece = piece;
        this.sourceRow = piece.getRow();
        this.sourceCol = piece.getCol();
        this.targetRow = sourceRow;
        this.targetCol = sourceCol;
        this.length = 0;
    }
}
