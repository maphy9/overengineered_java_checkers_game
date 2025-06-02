package org.game.game;

import org.pieces.pieces.BlackMan;
import org.pieces.pieces.Piece;
import org.pieces.pieces.WhiteMan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.checkers.checkers.Main.BOARD_SIZE;

public abstract class Player {
    private final List<Piece> pieces = new ArrayList<>();
    private Player enemy;
    private volatile Piece selectedPiece;
    private volatile int selectedRow = -1;
    private volatile int selectedCol = -1;
    private int score = 0;
    public final AtomicInteger remainingTime = new AtomicInteger();
    private List<AttackSequence> longestAttackSequences = new ArrayList<>();

    public abstract void turn() throws InterruptedException;

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

    public static void initializeWhitePieces(Player player) {
        List<Piece> pieces = player.getPieces();
        pieces.clear();

        for (int row = BOARD_SIZE; row >= 7; row--) {
            for (int col = 1; col <= BOARD_SIZE; col++) {
                if (row % 2 == 0 && col % 2 == 1 || row % 2 == 1 && col % 2 == 0) {
                    pieces.add(new WhiteMan(row, col, player));
                }
            }
        }
    }

    public static void initializeBlackPieces(Player player) {
        List<Piece> pieces = player.getPieces();
        pieces.clear();

        for (int row = 1; row <= 4; row++) {
            for (int col = 1; col <= BOARD_SIZE; col++) {
                if (row % 2 == 0 && col % 2 == 1 || row % 2 == 1 && col % 2 == 0) {
                    pieces.add(new BlackMan(row, col, player));
                }
            }
        }
    }
}
