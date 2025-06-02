package org.game.game;

import org.pieces.pieces.Piece;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.checkers.checkers.Main.BOARD_SIZE;

public class AIPlayer extends Player {
    private final Random random = new Random();

    @Override
    public void turn() throws InterruptedException {
        int waitTime = random.nextInt(3) + 1;
        TimeUnit.SECONDS.sleep(waitTime);

        List<AttackSequence> attackSequences = getLongestAttackSequences();

        if (!attackSequences.isEmpty()) {
            AttackSequence selectedAttack = attackSequences.get(random.nextInt(attackSequences.size()));

            setSelectedPiece(selectedAttack.piece);
            setSelectedRow(selectedAttack.targetRow);
            setSelectedCol(selectedAttack.targetCol);

            selectedAttack.piece.move(selectedAttack.targetRow, selectedAttack.targetCol);
            return;
        }

        List<Piece> pieces = getPieces();
        if (pieces.isEmpty()) {
            return;
        }

        while (true) {
            Piece randomPiece = pieces.get(random.nextInt(pieces.size()));

            int targetRow = random.nextInt(BOARD_SIZE - 1) + 1;
            int targetCol = random.nextInt(BOARD_SIZE - 1) + 1;

            if (randomPiece.canMove(targetRow, targetCol)) {
                setSelectedPiece(randomPiece);
                setSelectedRow(targetRow);
                setSelectedCol(targetCol);

                randomPiece.move(targetRow, targetCol);
                break;
            }
        }
    }
}

