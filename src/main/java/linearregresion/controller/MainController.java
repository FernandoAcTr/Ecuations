package linearregresion.controller;

import com.jfoenix.controls.JFXTabPane;
import ecuationsolutions.model.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import linearregresion.model.Summation;
import linearregresion.model.XYPoint;
import utils.MyUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<XYPoint> tablePoints;

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private Button btnGenerate, btnSolve;

    @FXML
    private Label lblXm, lblYm, lblXSum, lblYSum, lblXYSum, lblSquareXSum, lblSrSum, lblStSum, lblEcuation, lblR;

    @FXML
    private MenuItem mnuClose, mnuNew, mnuAbout, mnuHowFillData, mnuHowSolve;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    LineChart<Number, Number> lineChart;

    private ObservableList<XYPoint> listPoints;
    private Summation summation;

    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();
    }

    private void initData() {
        listPoints = FXCollections.observableArrayList();
    }

    private void initComponents() {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE));
        tablePoints.setDisable(true);
        tablePoints.setFocusTraversable(false);
        btnSolve.setDisable(true);
        tabPane.getTabs().get(1).setDisable(true);

        initTable();
        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                generateRows(spinner.getValue());
                btnGenerate.setDisable(true);
                spinner.setDisable(true);
            }
        });

        btnSolve.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    solveModel();
                    showGraphic();
                    tabPane.getTabs().get(1).setDisable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mnuClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/common_res/layout_principal.fxml"));
                    Scene scene = new Scene(root, 730, 600);
                    scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);
                    primaryStage.show();

                    ((Stage) btnSolve.getParent().getScene().getWindow()).close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mnuNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                clearAll();
            }
        });

        mnuAbout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                MyUtils.showAbouWindow();
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

    }

    private void initTable() {
        TableColumn<XYPoint, TextField> colX = new TableColumn("X");
        TableColumn<XYPoint, TextField> colY = new TableColumn("Y");
        TableColumn<XYPoint, String> colXY = new TableColumn<XYPoint, String>("XY");
        TableColumn<XYPoint, String> colSquareX = new TableColumn<XYPoint, String>("X^2");
        TableColumn<XYPoint, String> colST = new TableColumn<XYPoint, String>("St");
        TableColumn<XYPoint, String> colSR = new TableColumn<XYPoint, String>("Sr");

        colX.setCellValueFactory(new PropertyValueFactory<XYPoint, TextField>("txtPointX"));
        colY.setCellValueFactory(new PropertyValueFactory<XYPoint, TextField>("txtPointY"));
        colXY.setCellValueFactory(new PropertyValueFactory<XYPoint, String>("XY"));
        colSquareX.setCellValueFactory(new PropertyValueFactory<XYPoint, String>("squareX"));
        colSR.setCellValueFactory(new PropertyValueFactory<XYPoint, String>("SR"));
        colST.setCellValueFactory(new PropertyValueFactory<XYPoint, String>("ST"));

        colX.setSortable(false);
        colY.setSortable(false);
        colSquareX.setSortable(false);
        colXY.setSortable(false);
        colSR.setSortable(false);
        colST.setSortable(false);

        colSquareX.setPrefWidth(130);
        colXY.setPrefWidth(130);
        colX.setPrefWidth(130);
        colY.setPrefWidth(130);
        colST.setPrefWidth(130);
        colSR.setPrefWidth(130);

        colXY.setEditable(true);

        tablePoints.getColumns().addAll(colX, colY, colSquareX, colXY, colSR, colST);

        tablePoints.setItems(listPoints);
    }

    /**
     * Genera las filas de la tabla vacias
     */
    private void generateRows(int numRows) {
        listPoints.clear();
        for (int i = 0; i < numRows; i++)
            listPoints.add(new XYPoint(i, tablePoints));

        tablePoints.setDisable(false);
        btnSolve.setDisable(false);
    }

    /**
     * Resuelve el modelo de regrtesion Lineal
     */
    private void solveModel() {
        float a1, a0;
        summation = new Summation(listPoints);

        lblXSum.setText(MyUtils.format(summation.getXSum()));
        lblYSum.setText(MyUtils.format(summation.getYSum()));
        lblSquareXSum.setText(MyUtils.format(summation.getSquareXSum()));
        lblXYSum.setText(MyUtils.format(summation.getXYSum()));
        lblXm.setText(MyUtils.format(summation.getXm()));
        lblYm.setText(MyUtils.format(summation.getYm()));

        a1 = summation.getA1();
        a0 = summation.getA0();

        lblEcuation.setText("y = " + a0 + " + " + a1 + "x");

        for (XYPoint xyPoint : listPoints) {
            xyPoint.setSR(a0, a1);
            xyPoint.setST(summation.getYm());
        }

        tablePoints.refresh();

        summation.setSRSum();
        summation.setSTSum();

        lblSrSum.setText(summation.getSRSum() + "");
        lblStSum.setText(summation.getSTSum() + "");

        lblR.setText(MyUtils.format(summation.getR()) + "");
    }

    private void showGraphic() throws Exception {
        lineChart.getData().clear();

        String def = summation.getA0() + " + " + summation.getA1() + "x";
        Function function = new Function(def);

        double xValues[] = new double[listPoints.size()];
        double yValues[];

        for (int i = 0; i < listPoints.size(); i++)
            xValues[i] = listPoints.get(i).getX();

        yValues = function.evaluateFrom(xValues);

        XYChart.Series pointsSerie = getSerie();
        XYChart.Series linearSerie = getSerie(def, xValues, yValues);

        lineChart.getData().add(pointsSerie);
        lineChart.getData().add(linearSerie);
    }

    private XYChart.Series getSerie() {
        XYChart.Series<Double, Double> serie = new XYChart.Series<Double, Double>();
        serie.setName("Points");

        for (XYPoint xyPoint : listPoints)
            serie.getData().add(new XYChart.Data<Double, Double>((double) xyPoint.getX(), (double) xyPoint.getY()));

        return serie;
    }

    private XYChart.Series getSerie(String name, double[] xValues, double[] yValues) {
        XYChart.Series<Double, Double> serie = new XYChart.Series<Double, Double>();
        serie.setName("y = " + name);

        for (int i = 0; i < xValues.length; i++)
            serie.getData().add(new XYChart.Data<Double, Double>(xValues[i], yValues[i]));

        return serie;
    }

    private void clearAll() {
        lblStSum.setText("");
        lblSrSum.setText("");
        lblEcuation.setText("");
        lblXYSum.setText("");
        lblXm.setText("");
        lblYm.setText("");
        lblXSum.setText("");
        lblYSum.setText("");
        lblR.setText("");
        lblSquareXSum.setText("");
        tabPane.getTabs().get(1).setDisable(true);
        tabPane.getSelectionModel().selectFirst();
        tablePoints.getItems().clear();
        tablePoints.setDisable(true);
        btnGenerate.setDisable(false);
        spinner.setDisable(false);
    }

}
