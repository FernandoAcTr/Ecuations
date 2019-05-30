import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController implements Initializable {

    @FXML
    private JFXButton btnCloseMethods;

    @FXML
    private JFXButton btnOpenMethods;

    @FXML
    private JFXButton btnLinearSystem;

    @FXML
    private JFXButton btnNoLinearSystem;

    @FXML
    private JFXButton btnLinearRegresion;

    @FXML
    private JFXButton btnPolynomial;

    @FXML
    private JFXButton btnMultiple;

    @FXML
    private JFXButton btnInterpolation;

    @FXML
    private JFXButton btnExit;


    private onItemClick itemClick;

    private String userName;

    public SideMenuController(onItemClick itemClick){
        this.itemClick = itemClick;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnOpenMethods.setOnAction(event -> itemClick.onOpenMethodClick());

        btnCloseMethods.setOnAction(event -> itemClick.onCloseMethodClick());

        btnLinearRegresion.setOnAction(event -> itemClick.onLinearRegresionClick());

        btnPolynomial.setOnAction(event -> itemClick.onPolynomialRegresionClick());

        btnLinearSystem.setOnAction(event -> itemClick.onLinearSystemClick());

        btnNoLinearSystem.setOnAction(event -> itemClick.onNoLinearSystemClick());

        btnExit.setOnAction(event -> System.exit(0));

        btnMultiple.setOnAction(event -> itemClick.onMultipleRegresionClick());

        btnInterpolation.setOnAction(event -> itemClick.onInterpolationClick());
    }

    public interface onItemClick{
        void onCloseMethodClick();
        void onOpenMethodClick();
        void onLinearSystemClick();
        void onNoLinearSystemClick();
        void onLinearRegresionClick();
        void onPolynomialRegresionClick();
        void onMultipleRegresionClick();
        void onInterpolationClick();
    }
}
