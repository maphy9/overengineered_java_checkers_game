package org.game.game;

import org.pieces.pieces.Piece;

public class LocalWhitePlayer extends WhitePlayer {
    @Override
    public void turn() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            Piece selectedPiece = getSelectedPiece();
            if (selectedPiece == null) {
                Thread.sleep(200);
                continue;
            }
            int selectedRow = getSelectedRow();
            int selectedCol = getSelectedCol();
            if (selectedRow == -1 || selectedCol == -1) {
                Thread.sleep(200);
                continue;
            }
            if (selectedPiece.canMove(selectedRow, selectedCol)) {
                selectedPiece.move(selectedRow, selectedCol);
                setSelectedRow(-1);
                setSelectedCol(-1);
                setSelectedPiece(null);
                break;
            }
            Thread.sleep(200);
        }
    }
}
