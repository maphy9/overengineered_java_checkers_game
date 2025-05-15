package org.game.game;

import org.pieces.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final List<Piece> pieces = new ArrayList<>();
    private Player enemy;
    private volatile Piece selectedPiece;
    private volatile int selectedRow = -1;
    private volatile int selectedCol = -1;
    private int score = 0;
    private List<AttackSequence> longestAttackSequences = new ArrayList<>();

    public abstract void initializePieces();

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

    public List<Piece> getPieces() {
        return pieces;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public void setSelectedCol(int selectedCol) {
        this.selectedCol = selectedCol;
    }

    public Player getEnemy() {
        return enemy;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }

    public void setLongestAttackSequences(char[][] board) {
        List<AttackSequence> longestAttackSequences = new ArrayList<>();

        for (Piece piece : pieces) {
            List<AttackSequence> attackSequences = piece.getLongestAttackSequences(board);
            if (attackSequences.isEmpty()) {
                continue;
            }
            if (longestAttackSequences.isEmpty()) {
                longestAttackSequences = attackSequences;
            } else if (longestAttackSequences.getFirst().length < attackSequences.getFirst().length) {
                longestAttackSequences = attackSequences;
            } else if (longestAttackSequences.getFirst().length == attackSequences.getFirst().length) {
                longestAttackSequences.addAll(attackSequences);
            }
        }

        this.longestAttackSequences = longestAttackSequences;
    }

    public List<AttackSequence> getLongestAttackSequences() {
        return longestAttackSequences;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore(int delta) {
        score += delta;
    }
}
