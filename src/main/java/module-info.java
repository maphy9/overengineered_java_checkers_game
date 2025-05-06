module org.checkers.checkers {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.checkers.checkers to javafx.fxml;
    exports org.checkers.checkers;
    opens org.game.game to javafx.fxml;
    exports org.game.game;
    exports org.checkers.checkers.elementControllers;
    opens org.checkers.checkers.elementControllers to javafx.fxml;
    exports org.pieces.pieces;
    opens org.pieces.pieces to javafx.fxml;
}