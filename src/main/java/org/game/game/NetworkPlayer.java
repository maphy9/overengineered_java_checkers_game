package org.game.game;

import org.pieces.pieces.Piece;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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
                            selectedCol,
                            remainingTime.get(),
                            getEnemy().remainingTime.get()
                    );
                    output.writeObject(moveData);
                    output.flush();
                    output.reset();
                } catch (IOException e) {
                    System.err.println("Error sending move: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }

                break;
            }

            Thread.sleep(200);
        }
    }

    private void waitForRemoteMove() {
        int retryCount = 0;
        final int MAX_RETRIES = 3;

        try {
            socket.setSoTimeout(1000);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    MoveData moveData = (MoveData) input.readObject();

                    remainingTime.set(moveData.remainingTime);
                    getEnemy().remainingTime.set(moveData.enemyRemainingTime);

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
                    } else {
                        System.err.println("Received invalid move from remote player");
                    }
                    retryCount = 0;
                    break;
                } catch (java.net.SocketTimeoutException e) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                } catch (ClassNotFoundException | IOException e) {
                    System.err.println("Error receiving move (attempt " + (retryCount+1) + "/" + MAX_RETRIES + "): " + e.getMessage());
                    retryCount++;

                    if (retryCount >= MAX_RETRIES) {
                        System.err.println("Failed to receive move after " + MAX_RETRIES + " attempts");
                        Thread.currentThread().interrupt();
                        break;
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    try {
                        socket.setSoTimeout(100);
                    } catch (SocketException se) {
                        System.err.println("Failed to reset socket timeout: " + se.getMessage());
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Fatal error with socket: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static class MoveData implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        public final int originalRow;
        public final int originalCol;
        public final int targetRow;
        public final int targetCol;
        public final int remainingTime;
        public final int enemyRemainingTime;


        public MoveData(int originalRow, int originalCol, int targetRow, int targetCol, int remainingTime, int enemyRemainingTime) {
            this.originalRow = originalRow;
            this.originalCol = originalCol;
            this.targetRow = targetRow;
            this.targetCol = targetCol;
            this.remainingTime = remainingTime;
            this.enemyRemainingTime = enemyRemainingTime;
        }
    }
}
