package org.checkers.checkers;

import org.game.game.Game;
import org.game.game.NetworkPlayer;
import org.game.game.Player;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class NetworkGameSceneController extends GameSceneController {
    private Socket clientSocket = null;
    private boolean isHost = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @Override
    public void cleanup() {
        if (gameThread != null) {
            gameThread.interrupt();
        }

        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    public void setNetworkConnection(Socket clientSocket, boolean isHost) {
        this.clientSocket = clientSocket;
        this.isHost = isHost;

        try {
            Player whitePlayer;
            Player blackPlayer;

            if (isHost) {
                whitePlayer = new NetworkPlayer(clientSocket, true);
                blackPlayer = new NetworkPlayer(clientSocket, false);
            } else {
                whitePlayer = new NetworkPlayer(clientSocket, false);
                blackPlayer = new NetworkPlayer(clientSocket, true);
            }

            Player.initializeWhitePieces(whitePlayer);
            Player.initializeBlackPieces(blackPlayer);

            game = new Game(this, whitePlayer, blackPlayer);

            gameThread = new Thread(game);
            gameThread.start();
        } catch (IOException e) {
            System.err.println("Error setting up network connection: " + e.getMessage());
        }
    }
}
