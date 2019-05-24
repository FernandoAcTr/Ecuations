package polynomialregresion.controller;

import com.jfoenix.controls.JFXTabPane;
import ecuationsolutions.model.Function;
import ecuationsystems.model.SystemSolver;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import polynomialregresion.model.Summation;
import polynomialregresion.model.TableUtils;
import polynomialregresion.model.XYPoint;
import utils.MyUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TableView<XYPoint> tablePoints;

    @FXML
    private TableView<List<String>> dynamicTable;

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private Button btnGenerate, btnSolve;

    @FXML
    private Label lblYm, lblR;

    @FXML
    private MenuItem mnuNew, mnuAbout, mnuHowFillData, mnuHowSolve, mnuGrade, mnuHowConfigure;

    @FXML
    private JFXTabPane tabPane;

    @FXML
    LineChart<Number, Number> lineChart;

    @FXML
    private VBox paneCoeficients;

    private ObservableList<XYPoint> listPoints;
    private Summation summation;
    private SystemSolver systemSolver;
    private Function ecuation;
    private int grade;
    private boolean sumDone;


    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initComponents();
    }

    private void initData() {
        grade = 2;
        sumDone = false;
        listPoints = FXCollections.observableArrayList();
        systemSolver = new SystemSolver();
        setDynamicTableColumns();
    }

    private void initComponents() {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE));
        tablePoints.setDisable(true);
        tablePoints.setFocusTraversable(false);
        dynamicTable.setFocusTraversable(false);
        dynamicTable.setDisable(true);
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
                    lineChart.setTitle("Ajuste de polinomio de grado "+grade);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mnuNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                clearAll();
            }
        });

        mnuGrade.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                int lastGrade = grade;
                showDialogGrade();
                if(grade != lastGrade) setDynamicTableColumns();
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

        mnuHowConfigure.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String help = "Dirígase a \"Configurar>>Grado del Polinomio\"." +
                        "\nSe desplegará una ventana en donde podra cambiar" +
                        "\nel grado del polinomio con el que desee trabajar";
                MyUtils.showHelpMessage(help, 500, 150);
            }
        });


    }

    private void initTable() {
        TableColumn<XYPoint, TextField> colX = new TableColumn("X");
        TableColumn<XYPoint, TextField> colY = new TableColumn("Y");

        colX.setCellValueFactory(new PropertyValueFactory<>("txtPointX"));
        colY.setCellValueFactory(new PropertyValueFactory<>("txtPointY"));

        colX.setSortable(false);
        colX.setResizable(false);
        colY.setSortable(false);
        colY.setResizable(false);

        colX.setPrefWidth(130);
        colY.setPrefWidth(130);

        tablePoints.getColumns().addAll(colX, colY);

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
        dynamicTable.setDisable(false);
        btnSolve.setDisable(false);
    }

    /**
     * Resuelve el modelo de regrtesion Polinomial
     */
    private void solveModel() {
        dynamicTable.getItems().clear();

        if(sumDone) {
            listPoints.remove(listPoints.size()-1);
            sumDone = false;
        }
        summation = new Summation(listPoints);
        ArrayList<ArrayList<String>> XPotenceColumns = new ArrayList<>();
        ArrayList<ArrayList<String>> XYPotenceColumns = new ArrayList<>();
        ArrayList<String> SrValues, StValues;
        ArrayList<String> totals = new ArrayList<>();

        int i, indexRow, indexColumn;
        double total;

        //Ciclo para sumar las X^2, X^3, X^4...
        for (i = 0; i < grade * 2 - 1; i++) {
            ArrayList<String> column = summation.getXPotenceValues(i+2);
            XPotenceColumns.add(column);
            total = summation.getXPotenceSum(i+2);
            totals.add(MyUtils.format(total));
        }

        //Ciclo para sumar las X^2Yi, X^3Yi, X^4Yi...
        for (i = 0; i < grade; i++){
            ArrayList<String> column = summation.getXYPotenceValues(i+1);
            XYPotenceColumns.add(column);
            total = summation.getXYPotenceSum(i+1);
            totals.add(MyUtils.format(total));
        }

        //obtener el sistema de ecuaciones en matriz de coeficientes y resolverlo
        double ecuations[][] = summation.getEcuations(grade);
        systemSolver.setMatrix(ecuations);
        systemSolver.setNumVariables(grade+1);
        systemSolver.solveByGauss();
        double coeficients[] = systemSolver.getGaussResults();

        //Obtener valores de Sr y St
        SrValues = summation.getSrValues(coeficients);
        StValues = summation.getStValues(summation.getYMedia());
        double Sr = summation.getValuesSum(SrValues);
        double St = summation.getValuesSum(StValues);

        totals.add(MyUtils.format(Sr));
        totals.add(MyUtils.format(St));

        if(!sumDone) {
            XYPoint totalPoint = new XYPoint(listPoints.size(), tablePoints);
            totalPoint.getTxtPointX().setEditable(false);
            totalPoint.getTxtPointY().setEditable(false);
            totalPoint.getTxtPointX().setStyle("-fx-background-color: rgba(205,220,57,0.3)");
            totalPoint.getTxtPointY().setStyle("-fx-background-color: rgba(205,220,57,0.3)");
            totalPoint.getTxtPointX().setText(MyUtils.format(summation.getXSum()));
            totalPoint.getTxtPointY().setText(MyUtils.format(summation.getYSum()));
            tablePoints.getItems().add(totalPoint);
            sumDone = true;
        }

        //obtener la funcion polinomial
        String fun = summation.getPolynomialEcuation(coeficients);
        ecuation = new Function(fun);

        //vaciar los arrayList en filas y agregarlas al la TableView dinamica
        ArrayList<String> row;
        for (indexRow = 0; indexRow < XPotenceColumns.get(0).size(); indexRow++) {
            row = new ArrayList<>();

            for (indexColumn = 0; indexColumn < (XPotenceColumns.size() + XYPotenceColumns.size() + 2); indexColumn++) {

                //Decision para recorrer las columnas X^2, X^3, X^4...
                if(indexColumn < XPotenceColumns.size()){
                    String value = XPotenceColumns.get(indexColumn).get(indexRow);
                    row.add(value);
                }
                //Decision para recorrer las columnas X^2Yi, X^3Yi, X^4Yi...
                else if(indexColumn < XPotenceColumns.size() + XYPotenceColumns.size()){
                    String value = XYPotenceColumns.get(indexColumn - XPotenceColumns.size()).get(indexRow);
                    row.add(value);
                }
                //Decision para recorrer las columnas Sr y St
                else{
                    row.add(SrValues.get(indexRow));
                    row.add(StValues.get(indexRow));
                }
            }

            dynamicTable.getItems().add(row);
        }

        dynamicTable.getItems().add(totals);
        showValuesInLabels(coeficients, St, Sr);
    }

    private void showValuesInLabels(double coeficients[], double St, double Sr){
        lblYm.setText(MyUtils.format(summation.getYMedia()));
        lblR.setText(MyUtils.format(summation.getR(St, Sr)));
        paneCoeficients.getChildren().clear();
        String c;
        for (int i = 0; i < coeficients.length; i++) {
            c = "a"+ i + " = " + MyUtils.format(coeficients[i]);
            paneCoeficients.getChildren().add(new Label(c));
        }
    }

    private void showGraphic() throws Exception {
        lineChart.getData().clear();

        XYChart.Series pointsSerie = getSerie();

        lineChart.getData().add(pointsSerie);
        addPolynomialGraphic();
    }

    private void addPolynomialGraphic(){
        try {
            double initX = listPoints.get(0).getX();
            double lastX = listPoints.get(listPoints.size()-2).getX();
            double xValues[] = ecuation.generateRange(initX, lastX, 800);
            double yValues[] = ecuation.evaluateFrom(xValues);
            XYChart.Series polynomial = getSerie(ecuation.getFunction(), xValues, yValues);
            lineChart.getData().add(polynomial);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private XYChart.Series getSerie() {
        XYChart.Series<Double, Double> serie = new XYChart.Series<Double, Double>();
        serie.setName("Points");

        for (int i = 0; i < listPoints.size() - 1; i++)
            serie.getData().add(new XYChart.Data<>((double) listPoints.get(i).getX(), (double) listPoints.get(i).getY()));

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
        lblYm.setText("");
        lblR.setText("");
        paneCoeficients.getChildren().clear();
        tabPane.getTabs().get(1).setDisable(true);
        tabPane.getSelectionModel().selectFirst();
        tablePoints.getItems().clear();
        dynamicTable.getItems().clear();
        dynamicTable.setDisable(true);
        tablePoints.setDisable(true);
        btnGenerate.setDisable(false);
        spinner.setDisable(false);
        sumDone = false;
    }

    private void showDialogGrade() {
        final Dialog<Integer> dialog = new Dialog<Integer>();
        dialog.setTitle("Grado");
        dialog.setContentText("Selecciona el grado del polinomio");

        final Spinner<Integer> spinner = new Spinner();
        final ButtonType buttonOk = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10));
        spinner.getValueFactory().setValue(grade);
        dialog.getDialogPane().setContent(spinner);
        dialog.getDialogPane().getButtonTypes().addAll(buttonOk, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonOk) {
                return spinner.getValue();
            }
            return null;
        });

        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent())
            grade = result.get();
    }

    private void setDynamicTableColumns() {
        String headers[] = TableUtils.getColumnHeaders(grade);
        TableColumn<List<String>, String> column;

        dynamicTable.getColumns().clear();
        dynamicTable.getItems().clear();

        for (int i = 0; i < headers.length; i++) {
            column = new TableColumn<>(headers[i]);

            int finalI = i;
            column.setCellValueFactory((TableColumn.CellDataFeatures<List<String>, String> param) ->
                    new SimpleStringProperty(param.getValue().get(finalI))
            );

            column.setSortable(false);
            column.setPrefWidth(130);

            dynamicTable.getColumns().add(column);
        }

        //agregar una fila en blanco para que me deje hacer scroll horizontal
        ArrayList<String> blankArray = new ArrayList<>();
        for (int i = 0; i < headers.length; i++)
            blankArray.add("");

        dynamicTable.getItems().add(blankArray);
    }

}
