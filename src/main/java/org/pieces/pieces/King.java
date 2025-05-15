package org.pieces.pieces;

import javafx.scene.image.Image;
import org.game.game.AttackSequence;
import org.game.game.Player;

import java.util.ArrayList;
import java.util.List;

import static org.checkers.checkers.Main.BOARD_SIZE;

public class King extends Piece {
    public King(int row, int col, Image pieceImage, Player player) {
        super(row, col, pieceImage, player);
    }

    @Override
    public boolean canMove(int newRow, int newCol) {
        if (isPositionOccupied(newRow, newCol)) {
            return false;
        }

        if (!getOwner().getLongestAttackSequences().isEmpty()) {
            return canAttack(newRow, newCol);
        }

        int row = getRow() + 1;
        int col = getCol() + 1;
        while (row <= BOARD_SIZE && col <= BOARD_SIZE) {
            if (row == newRow && col == newCol) {
                return true;
            }
            row++;
            col++;
        }

        row = getRow() - 1;
        col = getCol() - 1;
        while (row > 0 && col > 0) {
            if (row == newRow && col == newCol) {
                return true;
            }
            row--;
            col--;
        }

        row = getRow() + 1;
        col = getCol() - 1;
        while (row <= BOARD_SIZE && col > 0) {
            if (row == newRow && col == newCol) {
                return true;
            }
            row++;
            col--;
        }

        row = getRow() - 1;
        col = getCol() + 1;
        while (row > 0 && col <= BOARD_SIZE) {
            if (row == newRow && col == newCol) {
                return true;
            }
            row--;
            col++;
        }

        return false;
    }

    @Override
    public void move(int newRow, int newCol) {
        AttackSequence attackSequence = getMoveAttackSequence(newRow, newCol);

        if (attackSequence == null) {
            setRow(newRow);
            setCol(newCol);
            return;
        }

        List<Piece> enemyPieces = getOwner().getEnemy().getPieces();
        List<int[]> capturedPositions = attackSequence.capturedPositions;
        List<Piece> removedPieces = new ArrayList<>();
        for (int[] capturedPosition : capturedPositions) {
            int capturedRow = capturedPosition[0];
            int capturedCol = capturedPosition[1];
            for (Piece enemyPiece : enemyPieces) {
                if (enemyPiece.getRow() == capturedRow && enemyPiece.getCol() == capturedCol) {
                    removedPieces.add(enemyPiece);
                }
            }
        }

        for (Piece removedPiece : removedPieces) {
            enemyPieces.remove(removedPiece);
        }

        getOwner().incrementScore(attackSequence.length);
        setRow(newRow);
        setCol(newCol);
    }

    private int[][] getAttackTargets(char[][] board) {
        int[][] attackTargets = new int[4][4];
        for (int[] attackTarget : attackTargets) {
            attackTarget[0] = -1;
            attackTarget[1] = -1;
            attackTarget[2] = -1;
            attackTarget[3] = -1;
        }

        int row = getRow();
        int col = getCol();
        while (row < BOARD_SIZE - 1 && col < BOARD_SIZE - 1) {
            if (board[row][col] == 'E' && board[row + 1][col + 1] == ' ') {
                attackTargets[0][0] = row + 1;
                attackTargets[0][1] = col + 1;
                attackTargets[0][2] = row + 2;
                attackTargets[0][3] = col + 2;
                break;
            } else if (board[row][col] == 'P') {
                break;
            }
            row++;
            col++;
        }

        row = getRow() - 2;
        col = getCol() - 2;
        while (row > 0 && col > 0) {
            if (board[row][col] == 'E' && board[row - 1][col - 1] == ' ') {
                attackTargets[1][0] = row + 1;
                attackTargets[1][1] = col + 1;
                attackTargets[1][2] = row;
                attackTargets[1][3] = col;
                break;
            } else if (board[row][col] == 'P') {
                break;
            }
            row--;
            col--;
        }

        row = getRow();
        col = getCol() - 2;
        while (row < BOARD_SIZE - 1 && col > 0) {
            if (board[row][col] == 'E' && board[row + 1][col - 1] == ' ') {
                attackTargets[2][0] = row + 1;
                attackTargets[2][1] = col + 1;
                attackTargets[2][2] = row + 2;
                attackTargets[2][3] = col;
                break;
            } else if (board[row][col] == 'P') {
                break;
            }
            row++;
            col--;
        }

        row = getRow() - 2;
        col = getCol();
        while (row > 0 && col < BOARD_SIZE - 1) {
            if (board[row][col] == 'E' && board[row - 1][col + 1] == ' ') {
                attackTargets[3][0] = row + 1;
                attackTargets[3][1] = col + 1;
                attackTargets[3][2] = row;
                attackTargets[3][3] = col + 2;
                break;
            } else if (board[row][col] == 'P') {
                break;
            }
            row--;
            col++;
        }

        return attackTargets;
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
        int[][] attackTargets = getAttackTargets(board);

        int targetRow = attackTargets[0][2] - 1;
        int targetCol = attackTargets[0][3] - 1;
        int captureRow = attackTargets[0][0] - 1;
        int captureCol = attackTargets[0][1] - 1;
        List<AttackSequence> attackBottomRight
                = getLongestAttackSequencesHelper(board, targetRow, targetCol, captureRow, captureCol);

        targetRow = attackTargets[1][2] - 1;
        targetCol = attackTargets[1][3] - 1;
        captureRow = attackTargets[1][0] - 1;
        captureCol = attackTargets[1][1] - 1;
        List<AttackSequence> attackBottomLeft
                = getLongestAttackSequencesHelper(board, targetRow, targetCol, captureRow, captureCol);

        targetRow = attackTargets[2][2] - 1;
        targetCol = attackTargets[2][3] - 1;
        captureRow = attackTargets[2][0] - 1;
        captureCol = attackTargets[2][1] - 1;
        List<AttackSequence> attackTopRight
                = getLongestAttackSequencesHelper(board, targetRow, targetCol, captureRow, captureCol);

        targetRow = attackTargets[3][2] - 1;
        targetCol = attackTargets[3][3] - 1;
        captureRow = attackTargets[3][0] - 1;
        captureCol = attackTargets[3][1] - 1;
        List<AttackSequence> attackTopLeft
                = getLongestAttackSequencesHelper(board, targetRow, targetCol, captureRow, captureCol);

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
