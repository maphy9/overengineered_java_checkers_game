package org.pieces.pieces;

import javafx.scene.image.Image;
import org.game.game.AttackSequence;
import org.game.game.Player;

import java.util.ArrayList;
import java.util.List;

import static org.checkers.checkers.Main.BOARD_SIZE;

public abstract class Man extends Piece {
    public Man(int row, int col, Image pieceImage, Player player) {
        super(row, col, pieceImage, player);
    }

    protected boolean canAttack(int newRow, int newCol) {
        List<AttackSequence> longestAttackSequences = getOwner().getLongestAttackSequences();
        if (!longestAttackSequences.isEmpty()) {
            for (AttackSequence attackSequence : longestAttackSequences) {
                Piece piece = attackSequence.piece;
                int targetRow = attackSequence.targetRow;
                int targetCol = attackSequence.targetCol;
                if (this == piece) {
                    if (newRow == targetRow && newCol == targetCol) {
                        return true;
                    }
                }
            }
            return false;
        }

        return true;
    }

    private List<AttackSequence> getLongestAttackSequencesHelper(char[][] board, int targetRow, int targetCol, int captureRow, int captureCol) {
        int row = getRow() - 1;
        int col = getCol() - 1;

        List<AttackSequence> attackSequences = new ArrayList<>();
        if (targetRow < BOARD_SIZE && targetCol < BOARD_SIZE && targetRow >= 0 && targetCol >= 0) {
            if (board[targetRow][targetCol] == ' ' && board[captureRow][captureCol] == 'E') {
                board[targetRow][targetCol] = 'P';
                board[captureRow][captureCol] = ' ';
                board[row][col] = ' ';
                setRow(targetRow + 1);
                setCol(targetCol + 1);
                List<AttackSequence> nextSequences = getLongestAttackSequences(board);
                setRow(row + 1);
                setCol(col + 1);
                board[targetRow][targetCol] = ' ';
                board[captureRow][captureCol] = 'E';
                board[row][col] = 'P';

                if (nextSequences.isEmpty()) {
                    AttackSequence tmp = new AttackSequence(this);
                    attackSequences.add(tmp);
                    tmp.targetRow = targetRow + 1;
                    tmp.targetCol = targetCol + 1;
                    tmp.length = 1;
                    tmp.capturedPositions.add(new int[] { captureRow + 1, captureCol + 1 });
                } else {
                    attackSequences = nextSequences;
                    for (AttackSequence attackSequence : nextSequences) {
                        attackSequence.sourceRow = row + 1;
                        attackSequence.sourceCol = col + 1;
                        attackSequence.length += 1;
                        attackSequence.capturedPositions.addFirst(new int[] { captureRow + 1, captureCol + 1 });
                    }
                }
            }
        }

        return attackSequences;
    }

    @Override
    public List<AttackSequence> getLongestAttackSequences(char[][] board) {
        int row = getRow() - 1;
        int col = getCol() - 1;

        List<AttackSequence> attackBottomRight
            = getLongestAttackSequencesHelper(board, row + 2, col + 2, row + 1, col + 1);
        List<AttackSequence> attackBottomLeft
            = getLongestAttackSequencesHelper(board, row + 2, col - 2, row + 1, col - 1);
        List<AttackSequence> attackTopRight
            = getLongestAttackSequencesHelper(board, row - 2, col + 2, row - 1, col + 1);
        List<AttackSequence> attackTopLeft
            = getLongestAttackSequencesHelper(board, row - 2, col - 2, row - 1, col - 1);

        List<AttackSequence> longestAttackSequence = attackBottomRight;

        if (longestAttackSequence.isEmpty()) {
            longestAttackSequence = attackBottomLeft;
        } else {
            if (!attackBottomLeft.isEmpty()) {
                if (longestAttackSequence.getFirst().length < attackBottomLeft.getFirst().length) {
                    longestAttackSequence = attackBottomLeft;
                } else if (longestAttackSequence.getFirst().length == attackBottomLeft.getFirst().length) {
                    longestAttackSequence.addAll(attackBottomLeft);
                }
            }
        }

        if (longestAttackSequence.isEmpty()) {
            longestAttackSequence = attackTopRight;
        } else {
            if (!attackTopRight.isEmpty()) {
                if (longestAttackSequence.getFirst().length < attackTopRight.getFirst().length) {
                    longestAttackSequence = attackTopRight;
                } else if (longestAttackSequence.getFirst().length == attackTopRight.getFirst().length) {
                    longestAttackSequence.addAll(attackTopRight);
                }
            }
        }

        if (longestAttackSequence.isEmpty()) {
            longestAttackSequence = attackTopLeft;
        } else {
            if (!attackTopLeft.isEmpty()) {
                if (longestAttackSequence.getFirst().length < attackTopLeft.getFirst().length) {
                    longestAttackSequence = attackTopLeft;
                } else if (longestAttackSequence.getFirst().length == attackTopLeft.getFirst().length) {
                    longestAttackSequence.addAll(attackTopLeft);
                }
            }
        }

        return longestAttackSequence;
    }
}
