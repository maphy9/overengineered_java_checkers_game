package org.pieces.pieces;

import javafx.scene.image.Image;
import org.game.game.AttackSequence;
import org.game.game.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    private int row;
    private int col;
    private final Image image;
    private final Player owner;

    public Piece(int row, int col, Image pieceImage, Player owner) {
        this.row = row;
        this.col = col;
        this.owner = owner;
        image = pieceImage;
    }

    public abstract boolean canMove(int newRow, int newCol);

    public void move(int newRow, int newCol) {
        row = newRow;
        col = newCol;
    }

    protected boolean isPositionOccupied(int newRow, int newCol) {
        Player owner = getOwner();
        List<Piece> friendlyPieces = new ArrayList<>(owner.getPieces());
        List<Piece> enemyPieces = new ArrayList<>(owner.getEnemy().getPieces());

        for (Piece piece : friendlyPieces) {
            if (piece.getRow() == newRow && piece.getCol() == newCol) {
                return true;
            }
        }

        for (Piece piece : enemyPieces) {
            if (piece.getRow() == newRow && piece.getCol() == newCol) {
                return true;
            }
        }

        return false;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Image getImage() {
        return image;
    }

    public Player getOwner() {
        return owner;
    }

    public abstract List<AttackSequence> getLongestAttackSequences(char[][] board);
}
