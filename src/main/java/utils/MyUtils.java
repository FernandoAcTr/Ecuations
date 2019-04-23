package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class MyUtils {

    private static DecimalFormat formatter = new DecimalFormat("#0.000000");

    public static void showHelpMessage(String helpMessage, int width, int heigth) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.getStyleClass().setAll("alert", "alert-info");

        Label lblInfo = new Label(helpMessage);
        final Button btnAcept = new Button("Aceptar");
        btnAcept.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                ((Stage) btnAcept.getScene().getWindow()).close();
            }
        });

        root.getChildren().addAll(lblInfo, btnAcept);
        Scene scene = new Scene(root, width, heigth);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void showMessage(String message, String title, String header, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }

    public static String format(double num){
        return formatter.format(num);
    }

    public static String format(float num){
        return formatter.format(num);
    }
}
