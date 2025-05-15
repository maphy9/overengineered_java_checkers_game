package org.checkers.checkers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import org.game.game.Game;
import org.game.game.Player;
import org.pieces.pieces.Piece;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.checkers.checkers.Main.BOARD_SIZE;

public class GameSceneController implements Initializable, SceneController {
    public Thread gameThread;
    private final Group whitePiecesGroup = new Group();
    private final Group blackPiecesGroup = new Group();
    Game game;

    private static final int PIECE_SIZE = 50;
    private static final int SQUARE_SIZE = 50;
    private static final String LIGHT_SQUARE_COLOR = "#FFCE9E";
    private static final String DARK_SQUARE_COLOR = "#D18B47";
    private static final String LABEL_COLOR = "#FFFFFF";

    @FXML
    private Pane gamePane;

    @FXML
    private Label whitePlayerScoreLabel;

    @FXML
    private Label blackPlayerScoreLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label whitePlayerTimeLeft;

    @FXML
    private Label blackPlayerTimeLeft;

    public void switchToMainScene(ActionEvent event) throws Exception {
        if (gameThread != null) {
            gameThread.interrupt();
        }

        URL mainSceneFxml = getClass().getResource("scenes/main-scene.fxml");
        FXMLLoader loader = new FXMLLoader(mainSceneFxml);
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void cleanup() {
        if (gameThread != null) {
            gameThread.interrupt();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (gameThread != null) {
            gameThread.interrupt();
        }

        game = new Game(this);

        initializeGamePane();

        gameThread = new Thread(game);
        gameThread.start();
    }

    public void initializeGamePane() {
        gamePane.getChildren().clear();
        whitePiecesGroup.getChildren().clear();
        blackPiecesGroup.getChildren().clear();
        drawBoard();
        gamePane.getChildren().add(whitePiecesGroup);
        gamePane.getChildren().add(blackPiecesGroup);
    }

    public void drawScores(int whitePlayerScore, int blackPlayerScore) {
        whitePlayerScoreLabel.setText(String.valueOf(whitePlayerScore));
        blackPlayerScoreLabel.setText(String.valueOf(blackPlayerScore));
    }

    public void drawMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
    }

    public void drawWhitePlayerTime(String time) {
        whitePlayerTimeLeft.setText(time);
    }

    public void drawBlackPlayerTime(String time) {
        blackPlayerTimeLeft.setText(time);
    }

    public void drawBoard() {
        for (int row = 1; row <= BOARD_SIZE; row++) {
            for (int col = 1; col <= BOARD_SIZE; col++) {
                Rectangle square = new Rectangle();
                square.setWidth(SQUARE_SIZE);
                square.setHeight(SQUARE_SIZE);

                square.setX(col * SQUARE_SIZE);
                square.setY(row * SQUARE_SIZE);

                if ((row + col) % 2 == 0) {
                    square.setFill(Color.valueOf(LIGHT_SQUARE_COLOR));
                } else {
                    square.setFill(Color.valueOf(DARK_SQUARE_COLOR));
                }

                square.setUserData(new int[]{row, col});

                square.setOnMouseClicked(event -> handleSquareClick(square));

                gamePane.getChildren().add(square);
            }
        }

        addCoordinateLabels();
    }

    private void handleSquareClick(Rectangle square) {
        Player activePlayer = game.getActivePlayer();
        if (activePlayer == null) {
            return;
        }
        if (activePlayer.getSelectedPiece() == null) {
            return;
        }
        int[] coordinates = (int[]) square.getUserData();
        activePlayer.setSelectedRow(coordinates[0]);
        activePlayer.setSelectedCol(coordinates[1]);
    }

    public void addCoordinateLabels() {
        for (int row = 1; row <= BOARD_SIZE; row++) {
            Label rowLabel = new Label(String.valueOf(row));
            rowLabel.setTextFill(Color.valueOf(LABEL_COLOR));

            rowLabel.setLayoutX(30);
            rowLabel.setLayoutY(row * SQUARE_SIZE + SQUARE_SIZE/2 - 10);
            rowLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            Label rowLabelRight = new Label(String.valueOf(row));
            rowLabelRight.setTextFill(Color.valueOf(LABEL_COLOR));
            rowLabelRight.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            rowLabelRight.setLayoutX(BOARD_SIZE * SQUARE_SIZE + 60);
            rowLabelRight.setLayoutY(row * SQUARE_SIZE + SQUARE_SIZE/2 - 10);

            gamePane.getChildren().addAll(rowLabel, rowLabelRight);
        }

        for (int col = 1; col <= BOARD_SIZE; col++) {
            char colChar = (char)('A' + col - 1);

            Label colLabel = new Label(String.valueOf(colChar));
            colLabel.setTextFill(Color.valueOf(LABEL_COLOR));
            colLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            colLabel.setLayoutX(col * SQUARE_SIZE + SQUARE_SIZE/2 - 5);
            colLabel.setLayoutY(BOARD_SIZE * SQUARE_SIZE + 60);

            Label colLabelTop = new Label(String.valueOf(colChar));
            colLabelTop.setTextFill(Color.valueOf(LABEL_COLOR));
            colLabelTop.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            colLabelTop.setLayoutX(col * SQUARE_SIZE + SQUARE_SIZE/2 - 5);
            colLabelTop.setLayoutY(30);

            gamePane.getChildren().addAll(colLabel, colLabelTop);
        }
    }

    public void drawPieces(Player whitePlayer, Player blackPlayer) {
        drawPiecesInGroup(whitePlayer, whitePiecesGroup);

        drawPiecesInGroup(blackPlayer, blackPiecesGroup);
    }

    public void drawPiecesInGroup(Player player, Group piecesGroup) {
        piecesGroup.getChildren().clear();
        List<Piece> pieces = player.getPieces();

        for (Piece piece : pieces) {
            Image pieceImage = piece.getImage();
            ImageView imageView = new ImageView();
            imageView.setFitHeight(PIECE_SIZE);
            imageView.setFitWidth(PIECE_SIZE);
            imageView.setImage(pieceImage);

            if (piece == player.getSelectedPiece()) {
                imageView.setOpacity(0.7);
            }

            imageView.setX(piece.getCol() * PIECE_SIZE);
            imageView.setY(piece.getRow() * PIECE_SIZE);

            imageView.setOnMouseClicked(event -> handlePieceClick(piece, player, piecesGroup));

            piecesGroup.getChildren().add(imageView);
        }
    }

    private void handlePieceClick(Piece piece, Player player, Group piecesGroup) {
        if (player != game.getActivePlayer()) {
            return;
        }
        if (piece == player.getSelectedPiece()) {
            player.setSelectedPiece(null);
        } else {
            player.setSelectedPiece(piece);
        }
        player.setSelectedRow(-1);
        player.setSelectedRow(-1);
        drawPiecesInGroup(player, piecesGroup);
    }
}
