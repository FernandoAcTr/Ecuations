package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.DecimalFormat;

public class MyUtils {

    private static double xOffset = 0;
    private static double yOffset = 0;

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

        undecorateWindow(stage, root, false);

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

    public static void undecorateWindow(final Stage stage, Parent root, boolean enableFullScreen){
        stage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        if(enableFullScreen) {
            stage.setFullScreen(enableFullScreen);
            root.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2)
                        stage.setFullScreen(!stage.isFullScreen());
                }
            });
        }

    }

    public static void showAbouWindow(){
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(MyUtils.class.getResource("/common_res/layout_about.fxml"));
            Scene scene = new Scene(root, 420, 360);
            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
