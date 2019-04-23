package ecuationsystems.controller;

import com.jfoenix.controls.JFXTabPane;
import ecuationsystems.model.ResolvMethods;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import utils.MyUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    GridPane paneTable;

    @FXML
    Button btnNumVariables, btnResolve, btnFillViwtZero;

    @FXML
    Spinner<Integer> spinNumVariable;

    @FXML
    private MenuItem mnuClose;

    @FXML
    TextArea textAreaSolution;

    @FXML
    ComboBox<String> cmbProcedure;

    @FXML
    JFXTabPane tabPane;

    @FXML
    TextField txtError;

    @FXML
    MenuItem mnuHowSelectMethod, mnuHowResolv, mnuHowFillData;

    int numVariables;
    ResolvMethods solver;

    public void initialize(URL location, ResourceBundle resources) {
        initGUI();
        initData();
        solver = new ResolvMethods();
    }

    public void initGUI() {
        paneTable.setVisible(false);
        btnNumVariables.setDisable(true);
        spinNumVariable.setDisable(true);
        btnResolve.setDisable(true);
        btnFillViwtZero.setDisable(true);
        txtError.setVisible(false);

        btnNumVariables.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btnNumVariables.getParent().getScene().getWindow();
                int diference = Math.abs(numVariables - spinNumVariable.getValue());

                if (spinNumVariable.getValue() > numVariables) {
                    stage.setHeight(stage.getHeight() + diference * 30);
                    stage.setWidth(stage.getWidth() + diference * 30);
                } else {
                    stage.setHeight(stage.getHeight() - +diference * 30);
                    stage.setWidth(stage.getWidth() - +diference * 30);
                }

                numVariables = spinNumVariable.getValue();
                setNumVariables(numVariables);
                paneTable.setVisible(true);
                btnResolve.setDisable(false);
                btnFillViwtZero.setDisable(false);
                textAreaSolution.clear();
                tabPane.getTabs().get(1).setDisable(true);
            }
        });

        mnuClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/common_res/layout_principal.fxml"));
                    Scene scene = new Scene(root, 730, 600);
                    scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);
                    primaryStage.show();

                    ((Stage) btnResolve.getParent().getScene().getWindow()).close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mnuHowSelectMethod.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String help = "Usted debe seleccionar el método por el cual desea resolver el sistema de ecuaciones" +
                        "\nen el combo box. De lo contrario no podrá realizar ninguna acción";
                MyUtils.showHelpMessage(help, 700, 150);
            }
        });

        mnuHowFillData.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String help = "Una vez se haya seleccionado el método, debe generar una matriz seleccionando" +
                        "\nel número de variables que tiene el sistema (máximo 10) y dando click en el " +
                        "\nbotón \"Generar Matriz\". El cuál generará una matriz de cajas de texto en donde" +
                        "\ndebe ingresar los coeficientes de las variables del sistema a resolver." +
                        "\nSi desea limpiar las cajas de texto totalmente basta con dar click nuevamente en" +
                        "\nel boton Generar Matriz.";
                MyUtils.showHelpMessage(help, 730, 210);

            }
        });

        mnuHowResolv.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String help = "Cuando la matriz este completamente llena (ninguna caja de texto debe estar vacía)" +
                        "\nDebe dar click en el botón \"Resolver\" y la pantalla cambiará de pestaña en donde se " +
                        "\nse mostrará la solución del sistema de ecuaciones.";
                MyUtils.showHelpMessage(help, 730, 200);
            }
        });

        btnResolve.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (verifyData()) {
                    btnResolveAction(cmbProcedure.getSelectionModel().getSelectedIndex());
                    tabPane.getTabs().get(1).setDisable(false);
                }
            }
        });

        btnFillViwtZero.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                fillWithZeroAction();
            }
        });

        cmbProcedure.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (cmbProcedure.getSelectionModel().getSelectedIndex()) {
                    case 0:
                    case 1:
                        txtError.setVisible(false);
                        break;
                    case 2:
                    case 3:
                        txtError.setVisible(true);
                        break;
                }
                btnNumVariables.setDisable(false);
                spinNumVariable.setDisable(false);
            }
        });

        cmbProcedure.getItems().add("Gauss");
        cmbProcedure.getItems().add("Gauss-Jordan");
        cmbProcedure.getItems().add("Jacobi");
        cmbProcedure.getItems().add("Gauss-Seidel");

        spinNumVariable.setTooltip(new Tooltip("Seleccione el número de variables del Sistema"));
        cmbProcedure.setTooltip(new Tooltip("Elija el método de solución"));

    }

    private void initData(){
        numVariables = 3;
        setNumVariables(numVariables);

        SpinnerValueFactory values = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10);
        values.setValue(numVariables);
        spinNumVariable.setValueFactory(values);
    }

    /**
     * Genera los TextField necesarios para el numero de variables
     *
     * @param numVariables
     */
    private void setNumVariables(int numVariables) {

        paneTable.getChildren().clear();
        paneTable.getColumnConstraints().clear();

        for (int i = 0; i < numVariables; i++) {
            Label lbl = new Label("X" + (i + 1));
            lbl.getStyleClass().add("lbl");
            lbl.getStyleClass().add("lbl-default");
            lbl.getStyleClass().add("h4");
            paneTable.add(lbl, i, 0);
        }

        for (int row = 1; row <= numVariables; row++)
            for (int col = 0; col < numVariables + 1; col++) {
                TextField txt = new TextField();
                txt.getStyleClass().add("text-primary");
                txt.setOnKeyTyped(keyEventNumber);
                paneTable.add(txt, col, row);
            }

        /*Set size to the columns
        one constraint for column, it is to say, I need add 3 constraint if I have 3 columns for example.
        paneTable know that a new constraint is for the next column*/
        for (int i = 0; i <= numVariables; i++) {
            ColumnConstraints constraint = new ColumnConstraints(50, 70, Double.MAX_VALUE);
            constraint.setHgrow(Priority.ALWAYS);
            paneTable.getColumnConstraints().add(constraint);
        }


    }

    /**
     * Obtiene todos los valores de las TextField y los convierte en una matriz
     *
     * @return
     */
    private double[][] getTableData() {
        double data[][] = new double[numVariables][numVariables + 1];
        int numTxt = numVariables;
        double num;

        for (int row = 0; row < numVariables; row++)
            for (int col = 0; col < numVariables + 1; col++, numTxt++) {
                num = Double.valueOf(((TextField) paneTable.getChildren().get(numTxt)).getText());
                data[row][col] = num;
            }
        return data;
    }

    /**
     * Revisa si todas las cajas de texto tienen algun valor
     *
     * @return false si falta alguna caja por llenarse
     */
    private boolean verifyData() {
        int numTxt = numVariables;
        boolean correct = true;
        TextField currentTextField;

        for (int row = 0; row < numVariables; row++)
            for (int col = 0; col < numVariables + 1; col++, numTxt++) {
                currentTextField = (TextField) paneTable.getChildren().get(numTxt);

                if (currentTextField.getText().length() == 0) {
                    correct = false;
                    currentTextField.setStyle("-fx-border-color: red; -fx-background-color: rgba(255, 0, 0, 0.2)");
                }
            }

        return correct;
    }

    /**
     * Rellena las TextField vacias con un 0
     */
    private void fillWithZeroAction() {
        int numTxt = numVariables;

        TextField currentTextField;

        for (int row = 0; row < numVariables; row++)
            for (int col = 0; col < numVariables + 1; col++, numTxt++) {
                currentTextField = (TextField) paneTable.getChildren().get(numTxt);

                if (currentTextField.getText().length() == 0) {
                    currentTextField.setStyle(null);
                    currentTextField.setText(0 + "");
                }
            }
    }

    private void btnResolveAction(int type) {
        double data[][];

        data = getTableData();
        solver.setMatrix(data);
        solver.setNumVariables(numVariables);

        if (type == 0)
            resolvGaussAction();

        else if (type == 1)
            resolvGaussJordanAction();

        else if (type == 2)
            if (txtError.getText().length() == 0) {
                showAlert("Advertencia", "Asegurate de ingresar el error permitido");
                return;
            } else
                resolvJacobiAction(Double.parseDouble(txtError.getText()));

        else if (type == 3)
            if (txtError.getText().length() == 0) {
                showAlert("Advertencia", "Asegurate de ingresar el error permitido");
                return;
            } else
                resolveGaussSeidelAction(Double.parseDouble(txtError.getText()));


        tabPane.getSelectionModel().selectNext();
    }

    private void resolvGaussAction() {
        double results[];

        boolean status = solver.resolvByGauss();
        results = solver.getGaussResults();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();

        if(status) {
            textAreaSolution.appendText("\nCon esto obtenemos la solución de la última incógnita. Usarla para formar expresiones con las " +
                    "filas anteriores, sustituir y resolver. Cada fila dará una solución para una incógnita\n");

            printResults(results);
        }
    }

    private void resolvGaussJordanAction() {
        double results[];

        boolean status = solver.resolvByGauss_Jordan();
        results = solver.getGaussJordanResults();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();

        if(status) {
            textAreaSolution.appendText("\nCon esto obtenemos la solución de todas las incógnitas donde el valor de cada una" +
                    "viene representado por el último valor de su respectiva fila en la matriz\n");

            printResults(results);
        }

    }

    private void resolvJacobiAction(double error) {

        solver.setErrorPermited(error);
        double results[] = solver.resolvByJacobi();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        solver.reestartProcedure();

        printResults(results);
    }

    private void resolveGaussSeidelAction(double error) {

        solver.setErrorPermited(error);
        double results[] = solver.resolvByGauss_Seidel();

        textAreaSolution.clear();
        textAreaSolution.setText(solver.getProcedure());
        if(results != null) printResults(results);
        solver.reestartProcedure();
    }

    private void printResults(double[] results) {
        for (int i = 0; i < results.length; i++)
            textAreaSolution.appendText("X" + (i + 1) + " = " + MyUtils.format(results[i]) + "\n");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.show();
    }

    EventHandler<KeyEvent> keyEventNumber = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent event) {
           if(Character.isLetter(event.getCharacter().charAt(0)))
                   event.consume();
        }
    };

    
}
