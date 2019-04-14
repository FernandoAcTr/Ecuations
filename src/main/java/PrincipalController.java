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

public class PrincipalController implements Initializable, EventHandler<MouseEvent>{

    @FXML
    Text descLinearSystem, descCloseMethods, descOpenMethods;

    @FXML
    Text descNoLinearSystem, descLinearRegresion;

    @FXML
    private Label lblinearEcuations, lblNoLinearEcuations, closeMethods, openMethods, lbllinearRegresion;

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

        String linearRegresionMessage = "En este apartado usted encontrará las" +
                "\nherramientas necesarias para la solución de " +
                "\nun modelo de regresión lineal.";
        descLinearRegresion.setText(linearRegresionMessage);


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

        lblinearEcuations.setOnMouseClicked(this);

        closeMethods.setOnMouseClicked(this);

        openMethods.setOnMouseClicked(this);

        lblNoLinearEcuations.setOnMouseClicked(this);

        lbllinearRegresion.setOnMouseClicked(this);
    }

    public void handle(MouseEvent event) {
        if(event.getSource() == lblinearEcuations)
            showLinearWindow();
        else if(event.getSource() == closeMethods)
            showCloseMethods();
        else if(event.getSource() == openMethods)
            showOpenMethods();
        else if(event.getSource() == lblNoLinearEcuations)
            showNoLinearSystem();
        else if(event.getSource() == lbllinearRegresion)
            showLinearRegresion();
    }

    private void showLinearWindow() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("ecuationsystem_res/fxml/main_layout.fxml"));
            Scene scene = new Scene(root, 780, 340);
            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add("/css/jfoenix-design.css");
            scene.getStylesheets().add("/css/jfoenix-fonts.css");
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Sistemas de ecuaciones lineales");
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
            root = FXMLLoader.load(getClass().getResource("ecuationsolution_res/fxml/layout_main.fxml"));
            Scene scene = new Scene(root, 780, 600);
            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add("/css/jfoenix-design.css");
            scene.getStylesheets().add("/css/jfoenix-fonts.css");
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Solucion de Ecuaciones: Metodos Cerrados");
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
            root = FXMLLoader.load(getClass().getResource("ecuationsolution_res/fxml/layout_main.fxml"));
            Scene scene = new Scene(root, 780, 600);
            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add("/css/jfoenix-design.css");
            scene.getStylesheets().add("/css/jfoenix-fonts.css");
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Solucion de Ecuaciones: Metodos abiertos");
            primaryStage.setScene(scene);
            primaryStage.show();

            ((Stage)lblinearEcuations.getParent().getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNoLinearSystem(){
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nolinearsystem_res/layout_main.fxml"));
        try {
            root = loader.load();
            Scene scene = new Scene(root, 780, 600);
            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add("/css/jfoenix-design.css");
            scene.getStylesheets().add("/css/jfoenix-fonts.css");
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Sistemas de ecuaciones no lineales: Metodo Newton-Raphson Multivariable");
            primaryStage.setScene(scene);
            primaryStage.show();

            ((Stage)lblinearEcuations.getParent().getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLinearRegresion(){
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("regresionlineal_res/layout_main.fxml"));
        try {
            root = loader.load();
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
            scene.getStylesheets().add("/css/jfoenix-design.css");
            scene.getStylesheets().add("/css/jfoenix-fonts.css");
            scene.getStylesheets().add("/regresionlineal_res/tablecss.css");
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Regresión Lineal");
            primaryStage.setScene(scene);
            primaryStage.show();

            ((Stage)lblinearEcuations.getParent().getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
