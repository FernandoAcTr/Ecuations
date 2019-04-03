import ecuationsolutions.controller.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {

    @FXML
    Text descLinearSystem, descCloseMethods, descOpenMethods;

    @FXML
    Text descNoLinearSystem;

    @FXML
    private Label lblinearEcuations, lblNoLinearEcuations, closeMethods, openMethods;

    @FXML
    private MenuItem mnuAbout, mnuClose;

    public void initialize(URL location, ResourceBundle resources) {
        initGUI();
    }

    private void initGUI() {
        String linearMessage = "En este apartado usted encontrara las \nherramientas necesarias para" +
                "la solución \nde sistemas de ecuaciones lineales de hasta 10x10";
        descLinearSystem.setText(linearMessage);

        String nolinearMessage = "En este apartado usted encontrara las \nherramientas necesarias para" +
                "la solución \nde sistemas de ecuaciones no lineales";
        descNoLinearSystem.setText(nolinearMessage);

        String closeMessage = "En este apartado usted encontrara las \nherramientas necesarias para" +
                "la solución \nde ecuaciones por métodos cerrados";
        descCloseMethods.setText(closeMessage);

        String openMessage = "En este apartado usted encontrara las \nherramientas necesarias para" +
                "la solución \nde ecuaciones por métodos abiertos";
        descOpenMethods.setText(openMessage);


        mnuAbout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/common_res/layout_about.fxml"));
                    Scene scene = new Scene(root, 420, 360);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mnuClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        lblinearEcuations.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                showLinearWindow();
            }
        });

        closeMethods.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                showCloseMethods();
            }
        });

        openMethods.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                showOpenMethods();
            }
        });
    }

    private void showLinearWindow() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ecuationsystem/fxml/main_layout.fxml"));
            Scene scene = new Scene(root, 780, 340);
            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add("/css/jfoenix-design.css");
            scene.getStylesheets().add("/css/jfoenix-fonts.css");
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();

            ((Stage)lblinearEcuations.getParent().getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCloseMethods(){
        Parent root = null;
        try {
            MainController.changeTypeMethods(0);
            root = FXMLLoader.load(getClass().getResource("./ecuationsolution/fxml/layout_main.fxml"));
            Scene scene = new Scene(root, 780, 600);
            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add("/css/jfoenix-design.css");
            scene.getStylesheets().add("/css/jfoenix-fonts.css");
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();

            ((Stage)lblinearEcuations.getParent().getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showOpenMethods(){
        Parent root = null;
        try {
            MainController.changeTypeMethods(1);
            root = FXMLLoader.load(getClass().getResource("./ecuationsolution/fxml/layout_main.fxml"));
            Scene scene = new Scene(root, 780, 600);
            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add("/css/jfoenix-design.css");
            scene.getStylesheets().add("/css/jfoenix-fonts.css");
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();

            ((Stage)lblinearEcuations.getParent().getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
