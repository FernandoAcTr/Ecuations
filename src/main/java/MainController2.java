import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.events.JFXDrawerEvent;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import ecuationsolutions.controller.MainController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController2 implements Initializable, SideMenuController.onItemClick {

    @FXML
    private JFXHamburger hamburguer;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private AnchorPane paneContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComponents();
    }

    private void initComponents(){
        initSideMenu();
        initHamburguer();
    }

    /*----------------------------------------------------------------------------------------------
                                        Init Methods
     ----------------------------------------------------------------------------------------------*/

    private void initSideMenu(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/common_res/side_bar.fxml"));
        try {
            SideMenuController controller = new SideMenuController(this);
            loader.setController(controller);
            Parent sideMenu = loader.load();
            drawer.setSidePane(sideMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initHamburguer(){
        HamburgerNextArrowBasicTransition transition = new HamburgerNextArrowBasicTransition(hamburguer);

        transition.setRate(-1);
        hamburguer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                transition.setRate(transition.getRate()*-1);
                transition.play();

                if(drawer.isClosed())
                    drawer.open();
                else
                    drawer.close();
            }
        });
    }

    /*----------------------------------------------------------------------------------------------
                                        SideMenuController Methods
     ----------------------------------------------------------------------------------------------*/

    @Override
    public void onCloseMethodClick() {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ecuationsolution_res/layout_main.fxml"));
        try {
            MainController controller = new MainController();
            MainController.changeTypeMethods(MainController.CLOSE_METHODS);

            loader.setController(controller);
            root = loader.load();

            paneContent.getChildren().setAll(root);
            attachToRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpenMethodClick() {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ecuationsolution_res/layout_main.fxml"));
        try {
            MainController controller = new MainController();
            MainController.changeTypeMethods(MainController.OPEN_METHODS);

            loader.setController(controller);
            root = loader.load();

            paneContent.getChildren().setAll(root);
            attachToRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLinearSystemClick() {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ecuationsystem_res/fxml/main_layout.fxml"));
        try {
            ecuationsystems.controller.MainController controller = new ecuationsystems.controller.MainController();
            loader.setController(controller);

            loader.setController(controller);
            root = loader.load();

            paneContent.getChildren().setAll(root);
            attachToRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNoLinearSystemClick() {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("nolinearsystem_res/layout_main.fxml"));
        try {
            nolinearsystems.controller.MainController controller = new nolinearsystems.controller.MainController();
            loader.setController(controller);

            loader.setController(controller);
            root = loader.load();

            paneContent.getChildren().setAll(root);
            attachToRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLinearRegresionClick() {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("linearregresion_res/layout_main.fxml"));
        try {
            linearregresion.controller.MainController controller = new linearregresion.controller.MainController();
            loader.setController(controller);

            loader.setController(controller);
            root = loader.load();

            paneContent.getChildren().setAll(root);
            attachToRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPolynomialRegresionClick() {
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("polynomialregresion_res/layout_main.fxml"));
        try {
            polynomialregresion.controller.MainController controller = new polynomialregresion.controller.MainController();
            loader.setController(controller);

            loader.setController(controller);
            root = loader.load();

            paneContent.getChildren().setAll(root);
            attachToRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void attachToRoot(Parent root){
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
    }

    /**-----------------------------------------------------------------------------------------------**/
}
