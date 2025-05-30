package org.game.game;

import org.pieces.pieces.Piece;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkPlayer extends Player {
    private final Socket socket;
    private final boolean isLocalPlayer;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    public NetworkPlayer(Socket socket, boolean isLocalPlayer) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Socket is closed or null");
        }

        this.socket = socket;
        this.isLocalPlayer = isLocalPlayer;

        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.output.flush();
        this.input = new ObjectInputStream(socket.getInputStream());
    }


    @Override
    public void turn() throws InterruptedException {
        if (isLocalPlayer) {
            makeLocalMove();
        } else {
            waitForRemoteMove();
        }
    }

    private void makeLocalMove() throws InterruptedException {
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
                int originalRow = selectedPiece.getRow();
                int originalCol = selectedPiece.getCol();

                selectedPiece.move(selectedRow, selectedCol);

                setSelectedRow(-1);
                setSelectedCol(-1);
                setSelectedPiece(null);

                try {
                    MoveData moveData = new MoveData(
                            originalRow,
                            originalCol,
                            selectedRow,
                            selectedCol
                    );
                    output.writeObject(moveData);
                    output.flush();
                } catch (IOException e) {
                    System.err.println("Error sending move: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }

                break;
            }

            Thread.sleep(200);
        }
    }

    private void waitForRemoteMove() throws InterruptedException {
        try {
            socket.setSoTimeout(500);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    MoveData moveData = (MoveData) input.readObject();

                    Piece pieceToMove = null;
                    for (Piece piece : getPieces()) {
                        if (piece.getRow() == moveData.originalRow &&
                                piece.getCol() == moveData.originalCol) {
                            pieceToMove = piece;
                            break;
                        }
                    }

                    if (pieceToMove != null && pieceToMove.canMove(moveData.targetRow, moveData.targetCol)) {
                        pieceToMove.move(moveData.targetRow, moveData.targetCol);
                        break;
                    } else {
                        System.err.println("Received invalid move from remote player");
                        break;
                    }
                } catch (java.net.SocketTimeoutException e) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error receiving move: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static class MoveData implements java.io.Serializable {
        private final int originalRow;
        private final int originalCol;
        private final int targetRow;
        private final int targetCol;

        public MoveData(int originalRow, int originalCol, int targetRow, int targetCol) {
            this.originalRow = originalRow;
            this.originalCol = originalCol;
            this.targetRow = targetRow;
            this.targetCol = targetCol;
        }
    }
}
