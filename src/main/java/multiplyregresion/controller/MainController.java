package multiplyregresion.controller;

import ecuationsystems.model.SystemSolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import multiplyregresion.model.MultiplePoint;
import multiplyregresion.model.Summation;
import utils.MyUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<MultiplePoint> tablePoints;

    @FXML
    private TableView<Summation.Results> sumTable;

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private Button btnGenerate, btnSolve;

    @FXML
    private Label lblYm;

    @FXML
    private Label lblR;

    @FXML
    private VBox paneCoeficients;

    @FXML
    private Label lblY, lblX1, lblX2, lblX1Square, lblX2Square, lblX1X2, lblX1Y, lblX2Y, lblA0, lblA1, lblA2, lblEcuation;

    @FXML
    private MenuItem mnuNew, mnuAbout, mnuHowFillData, mnuHowSolve;

    private ObservableList<MultiplePoint> listPoints;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();
    }

    private void initData() {
        listPoints = FXCollections.observableArrayList();
        tablePoints.setItems(listPoints);
    }

    private void initComponents() {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE));
        tablePoints.setDisable(true);
        tablePoints.setFocusTraversable(false);
        sumTable.setFocusTraversable(false);
        sumTable.setDisable(true);
        btnSolve.setDisable(true);

        mnuNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                clearAll();
            }
        });

        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                generateRows(spinner.getValue());
                btnGenerate.setDisable(true);
                spinner.setDisable(true);
            }
        });

        btnSolve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                solveModel();
            }
        });

        mnuHowFillData.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String help = "Usted debe selccionar cuántos datos va a graficar y posteriormente" +
                        "\ndar click en el botón \"Generar\", inmediatamente notará que la" +
                        "\ntabla se habilita. Debe entonces llenar los campos de las columnas" +
                        "\nX e Y con los puntos que desea graficar." +
                        "\nNotará que se desabilita el botón Generar por precaución para que no" +
                        "\nde un falso click y borre su información. Si quiere habilitarlo de nuevo" +
                        "\npara rehacer todo desde el inicio, de click en \"Archivo >> Nuevo\"";
                MyUtils.showHelpMessage(help, 570, 200);
            }
        });

        mnuHowSolve.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String help = "Cuando tenga la tabla debidamente llena con información" +
                        "\nde click en el botón \"Resolver\". Inmediatamente se habilitará" +
                        "\nla pestaña gráfica en donde podrá ver los puntos graficados y la" +
                        "\nlinea que mejor se ajusta a ellos.";
                MyUtils.showHelpMessage(help, 570, 160);
            }
        });

        addTablePointsColumns();
        addTableSummColumns();
    }

    private void addTablePointsColumns() {

        TableColumn<MultiplePoint, TextField> columnX1 = new TableColumn<>("X1");
        TableColumn<MultiplePoint, TextField> columnX2 = new TableColumn<>("X2");
        TableColumn<MultiplePoint, TextField> columnY = new TableColumn<>("Y");

        columnX1.setCellValueFactory(new PropertyValueFactory<>("txtPointX1"));
        columnX2.setCellValueFactory(new PropertyValueFactory<>("txtPointX2"));
        columnY.setCellValueFactory(new PropertyValueFactory<>("txtPointY"));

        tablePoints.getColumns().addAll(columnY, columnX1, columnX2);

        for (TableColumn c : tablePoints.getColumns()) {
            c.setSortable(false);
            c.setPrefWidth(130);
        }
    }

    private void addTableSummColumns() {
        TableColumn<Summation.Results, Float> colX1Square = new TableColumn<>("X1^2");
        TableColumn<Summation.Results, Float> colX2Square = new TableColumn<>("X2^2");
        TableColumn<Summation.Results, Float> colX1X2 = new TableColumn<>("X1*X2");
        TableColumn<Summation.Results, Float> colX1Y = new TableColumn<>("X1*Y");
        TableColumn<Summation.Results, Float> colX2Y = new TableColumn<>("X2*Y");

        colX1Square.setCellValueFactory(new PropertyValueFactory<>("X1Square"));
        colX2Square.setCellValueFactory(new PropertyValueFactory<>("X2Square"));
        colX1X2.setCellValueFactory(new PropertyValueFactory<>("X1X2"));
        colX1Y.setCellValueFactory(new PropertyValueFactory<>("X1Y"));
        colX2Y.setCellValueFactory(new PropertyValueFactory<>("X2Y"));

        sumTable.getColumns().addAll(colX1Square, colX2Square, colX1X2, colX1Y, colX2Y);

        for (TableColumn c : sumTable.getColumns()) {
            c.setSortable(false);
            c.setPrefWidth(130);
        }
    }

    /**
     * Genera las filas de la tabla vacias
     */
    private void generateRows(int numRows) {
        listPoints.clear();
        for (int i = 0; i < numRows; i++)
            listPoints.add(new MultiplePoint(i, tablePoints));

        tablePoints.setDisable(false);
        sumTable.setDisable(false);
        btnSolve.setDisable(false);
        tablePoints.refresh();
    }

    private void clearAll() {
        lblYm.setText("");
        lblR.setText("");
        lblX1.setText("");
        lblX2.setText("");
        lblY.setText("");
        lblX1Square.setText("");
        lblX2Square.setText("");
        lblX1X2.setText("");
        lblX1Y.setText("");
        lblX2Y.setText("");

        paneCoeficients.getChildren().clear();
        tablePoints.getItems().clear();
        sumTable.getItems().clear();
        sumTable.setDisable(true);
        tablePoints.setDisable(true);
        btnGenerate.setDisable(false);
        spinner.setDisable(false);
    }

    private void solveModel() {
        Summation summation = new Summation(listPoints);
        lblY.setText(MyUtils.format(summation.getYSum()));
        lblX1.setText(MyUtils.format(summation.getX1Sum()));
        lblX2.setText(MyUtils.format(summation.getX2Sum()));
        lblX1X2.setText(MyUtils.format(summation.getX1X2Sum()));
        lblX1Square.setText(MyUtils.format(summation.getX1SquareSum()));
        lblX2Square.setText(MyUtils.format(summation.getX2SquareSum()));
        lblX1Y.setText(MyUtils.format(summation.getX1YSum()));
        lblX2Y.setText(MyUtils.format(summation.getX2YSum()));
        lblYm.setText(MyUtils.format(summation.getYm()));

        ObservableList<Summation.Results> list = FXCollections.observableArrayList(summation.getListResults());
        sumTable.setItems(list);

        double n = listPoints.size();
        double x1 = summation.getX1Sum();
        double x2 = summation.getX2Sum();
        double x1Square = summation.getX1SquareSum();
        double x2Square = summation.getX2SquareSum();
        double y = summation.getYSum();
        double x1x2 = summation.getX1X2Sum();
        double x1y = summation.getX1YSum();
        double x2y = summation.getX2YSum();

        double matrix[][] = {
                {n, x1, x2, y},
                {x1, x1Square, x1x2, x1y},
                {x2, x1x2, x2Square, x2y}
        };

        SystemSolver systemSolver = new SystemSolver();
        systemSolver.setMatrix(matrix);
        systemSolver.setNumVariables(3);
        systemSolver.solveByGauss_Jordan();
        double results[] = systemSolver.getGaussJordanResults();

        lblA0.setText(MyUtils.format(results[0]));
        lblA1.setText(MyUtils.format(results[1]));
        lblA2.setText(MyUtils.format(results[2]));

        float r = summation.getR((float) results[0], (float) results[1], (float) results[2]);
        lblR.setText(MyUtils.format(r));

        String a0 = MyUtils.format(results[0]);
        String a1 = MyUtils.format(results[1]);
        String a2 = MyUtils.format(results[2]);
        String ec = "y = " + a0;
        ec += results[1] < 0 ? " " + a1 + "x1" : " + " + a1 + "x1";
        ec += results[2] < 0 ? " " + a2 + "x2" : " + " + a2 + "x2";
        lblEcuation.setText(ec);
    }
}
