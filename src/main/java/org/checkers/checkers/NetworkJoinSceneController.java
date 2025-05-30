package org.checkers.checkers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.ResourceBundle;

public class NetworkJoinSceneController implements Initializable, Cleanable {
    @FXML
    private Label myIPAddress;

    @FXML
    private Label myPort;

    @FXML
    private TextField connectIPField;

    @FXML
    private TextField connectPortField;

    private Thread serverThread;
    private Thread clientThread;

    public void switchToMainScene(ActionEvent event) throws Exception {
        cleanup();
        URL mainSceneFxml = getClass().getResource("scenes/main-scene.fxml");
        FXMLLoader loader = new FXMLLoader(mainSceneFxml);
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void connectToHost() {
        String opponentIPAddress = connectIPField.getText().trim();
        String opponentPort = connectPortField.getText().trim();

        int port;

        try {
            port = Integer.parseInt(opponentPort);
        } catch (Exception ignored) {
            return;
        }

        if (opponentIPAddress.isEmpty()) {
            return;
        }

        cleanup();

        Client client = new Client(opponentIPAddress, port);
        clientThread = new Thread(client);
        clientThread.start();
    }

    public void setMyIPAddress() {
        try {
            String ipAddress;
            InetAddress localhost = InetAddress.getLocalHost();
            ipAddress = localhost.getHostAddress();
            myIPAddress.setText(ipAddress);

            int port = new Random().nextInt(5000) + 5000;
            myPort.setText(port + "");

            Server server = new Server(port);
            serverThread = new Thread(server);
            serverThread.start();
        } catch (Exception e) {
            myIPAddress.setText("Error");
            myPort.setText("Error");
        }
    }

    public void switchToNetworkGameScene(Socket clientSocket, boolean isHost) throws IOException {
        URL networkGameSceneFxml = getClass().getResource("scenes/network-game-scene.fxml");
        FXMLLoader networkGameLoader = new FXMLLoader(networkGameSceneFxml);
        Parent root = networkGameLoader.load();
        NetworkGameSceneController controller = networkGameLoader.getController();
        controller.setNetworkConnection(clientSocket, isHost);

        Stage stage = (Stage) myIPAddress.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void cleanup() {
        if (serverThread != null) {
            serverThread.interrupt();
        }
        if (clientThread != null) {
            clientThread.interrupt();
        }

        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMyIPAddress();
    }

    private class Client implements Runnable {
        String ipAddress;
        int port;

        public Client(String ipAddress, int port) {
            this.ipAddress = ipAddress;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ipAddress, port), 5000);

                Platform.runLater(() -> {
                    try {
                        switchToNetworkGameScene(socket, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(NetworkJoinSceneController.this::setMyIPAddress);
            }
        }

    }

    private class Server implements Runnable {
            private int port;
            private ServerSocket serverSocket; // Keep a reference to the socket

            public Server(int port) {
                this.port = port;
            }

            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                    System.out.println("Server started on port " + port);

                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    Platform.runLater(() -> {
                        try {
                            switchToNetworkGameScene(clientSocket, true);
                        } catch (IOException e) {
                            System.err.println("Error switching to game scene: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Server error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
}