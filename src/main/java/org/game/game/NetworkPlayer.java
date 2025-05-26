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

        // Initialize streams
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.output.flush(); // Important to avoid deadlock
        this.input = new ObjectInputStream(socket.getInputStream());
    }


    @Override
    public void turn() throws InterruptedException {
        if (isLocalPlayer) {
            // Local player makes a move and sends it to remote player
            makeLocalMove();
        } else {
            // Remote player waits for move data from the network
            waitForRemoteMove();
        }
    }

    /**
     * Makes a move locally and sends it to the remote player
     */
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
                // Get the original position before moving
                int originalRow = selectedPiece.getRow();
                int originalCol = selectedPiece.getCol();

                // Execute the move locally
                selectedPiece.move(selectedRow, selectedCol);

                // Reset selection state
                setSelectedRow(-1);
                setSelectedCol(-1);
                setSelectedPiece(null);

                // Send move data to remote player
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

    /**
     * Waits for move data from the remote player and applies it locally
     */
    private void waitForRemoteMove() throws InterruptedException {
        try {
            // Set socket timeout to allow for thread interruption checks
            socket.setSoTimeout(500);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Wait for move data from remote player
                    MoveData moveData = (MoveData) input.readObject();

                    // Find the piece by its position
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
                    // This is expected - just check if thread is interrupted
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

    /**
     * Data class to represent a move for network transmission
     */
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
