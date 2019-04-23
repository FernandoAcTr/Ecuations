package ecuationsolutions.controller;

import com.jfoenix.controls.JFXTabPane;
import ecuationsolutions.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import utils.MyUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    private TextField txtFunction, txtFunctionMain;

    @FXML
    private Button btnShowGraphic, btnResolve;

    @FXML
    private TextField txtFrom;

    @FXML
    private TextField txtTo;

    @FXML
    private Label lblMethod;

    @FXML
    private TableView<ValuesBean> tableViewProcedure;

    @FXML
    private MenuItem mnuClose;

    @FXML
    private MenuItem mnuAbout;

    @FXML
    private MenuItem mnuNew;

    @FXML
    private MenuItem mnuOpen;

    @FXML
    private MenuItem mnuSave;

    @FXML
    private MenuItem mnuSaveAs;

    @FXML
    private MenuItem mnuHowGraphic, mnuHowResolv;

    @FXML
    LineChart<Number, Number> lineChart;

    @FXML
    private VBox paneMethod;

    @FXML
    private ComboBox<String> cmbMethod;

    @FXML
    private JFXTabPane tabPane;

    private TextField txtPointA, txtPointB, txtError;
    private TextField txtPointAOpen, txtDerived, txtGFunction;

    private HBox paneCloseMethod, paneNewtonMethod, paneFixedPointMethod, paneSecantMethod;

    ResolveMethod resolveMethod;
    FileFunction fileFunction;
    FileChooser fileChooser;

    public static final int CLOSE_METHODS = 0;
    public static final int OPEN_METHODS = 1;

    private static int typeMthods;

    public void initialize(URL location, ResourceBundle resources) {

        initData();
        initGUI();

    }

    private void initData() {
        resolveMethod = new ResolveMethod();
        fileFunction = new FileFunction();
        fileChooser = new FileChooser();
        fileChooser.setInitialFileName("*.func");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Function File", "*.func"));

        if (typeMthods == CLOSE_METHODS) {
            cmbMethod.getItems().add("Bisección");
            cmbMethod.getItems().add("Regla Falsa");
        } else {
            cmbMethod.getItems().add("Punto Fijo");
            cmbMethod.getItems().add("Newton-Raphson");
            cmbMethod.getItems().add("Método de la secante");
        }

        try {
            paneCloseMethod = FXMLLoader.load(getClass().getResource("/ecuationsolution_res/layout_closed_method.fxml"));
            paneNewtonMethod = FXMLLoader.load(getClass().getResource("/ecuationsolution_res/layout_newton_method.fxml"));
            paneFixedPointMethod = FXMLLoader.load(getClass().getResource("/ecuationsolution_res/layout_fixedpoint_method.fxml"));
            paneSecantMethod = FXMLLoader.load(getClass().getResource("/ecuationsolution_res/layout_secant_method.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        cmbMethod.getSelectionModel().selectFirst();

    }

    private void initGUI() {

        btnShowGraphic.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showGraphic();
            }
        });

        mnuClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/common_res/layout_principal.fxml"));
                    Scene scene = new Scene(root, 730,600);
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

        mnuNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                cleanAll();
            }
        });

        mnuSaveAs.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                mnuSaveAction((Stage) btnShowGraphic.getScene().getWindow(), false);
            }
        });

        mnuSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                mnuSaveAction((Stage) btnShowGraphic.getScene().getWindow(), true);
            }
        });

        mnuOpen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                cleanAll();
                mnuOpenAction((Stage) btnShowGraphic.getScene().getWindow());
            }
        });

        mnuHowGraphic.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String help = "Posicionados en la pestaña gáfica:" +
                        "\nUsted debe de ingresar la función que desea graficar y el intervalo numérico en el cuál se graficará"
                        + "\nposteriormente debe dar click en el botón graficar.";
                String information = "Para las funcioes se aceptan los siguientes simbolos: "
                        + "\n Potencias: ^"
                        + "\n Seno: sin(), sinh()"
                        + "\n Cosen: cos(), cosh()"
                        + "\n Tangente: tan()"
                        + "\n ArcTangente: atan()"
                        + "\n ArcSeno: asin()"
                        + "\n ArcCoseno: acos()"
                        + "\n Absoluto: abs()"
                        + "\n logaritmo base N: logn(x)"
                        + "\n logaritmo natural: log(x)"
                        + "\n Raiz: sqrt()"
                        + "\n Los signos de agrupacion aceptados son: (), {}, []"
                        + "\n Además se soporta la multiplicación implícita";
                MyUtils.showHelpMessage(help + "\n" + information, 700, 300);
            }
        });

        mnuHowResolv.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String help = "Posicionado en la pestaña Métodos de Solución:"
                        + "\nUsted debe ingresar la función de la cual desea encontrar su solución en la caja de texto superior. " +
                        "\nNote que si usted graficó anteriormente, la caja de texto superior replicará la función que graficó" +
                        "\npero puede modificarla libremente si así lo desea. " +
                        "\nPosteriormente debe seleccionar el método de solución y agregar los datos que sean necesarios para " +
                        "\ndicho método en específico en el panel inferior a la caja de texto de la función. " +
                        "\nUna vez haya llenado correctamente la información deberá dar click en el botón resolver." +
                        "\nNota: No es necesario graficar para resolver la ecuación";
                MyUtils.showHelpMessage(help, 750, 200);
            }
        });

        btnResolve.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                btnResolveAction();
            }
        });

        if (typeMthods == CLOSE_METHODS) {
            buildClosedPane();
            setColumnsCloseMethods();
        }
        else {
            buildFixedPointPane(); //el primero en el comboBox de metodos abiertos
            setColumnsFixedPoints();
        }


        cmbMethod.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (typeMthods){
                    case OPEN_METHODS:
                        if (cmbMethod.getSelectionModel().getSelectedIndex() == 0) {
                            buildFixedPointPane();
                            setColumnsFixedPoints();
                        }
                        else if (cmbMethod.getSelectionModel().getSelectedIndex() == 1) {
                           buildNewtonPane();
                           setColumnsNewtonRaphson();
                        }
                        else if (cmbMethod.getSelectionModel().getSelectedIndex() == 2) {
                            buildSecantPane();
                            setColumnsSecant();
                        }
                }
            }
        });

        txtFunction.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                txtFunctionMain.setText(txtFunction.getText());
            }
        });
    }

    public static void changeTypeMethods(int type) {
        typeMthods = type;
    }

    private void showGraphic() {
        double from, to;
        String def = txtFunction.getText();
        Function function = new Function(def);
        double increment;

        try {
            from = Double.parseDouble(txtFrom.getText());
            to = Double.parseDouble(txtTo.getText());
            increment = Math.abs(from - to) / 800;
        } catch (NumberFormatException ex) {
            MyUtils.showMessage("Ingresa correctamente el intervalo", "Error", null, Alert.AlertType.ERROR);
            return;
        }

        try {
            double xValues[] = function.generateRange(from, to, increment);
            double yValues[] = function.evaluateFrom(xValues);
            GraphicData graphicData = new GraphicData();
            XYChart.Series serie = graphicData.getSerie(def, xValues, yValues);

            lineChart.getData().clear();
            lineChart.getData().add(serie);

            final DecimalFormat formatter = new DecimalFormat("#.###");

            for (int i = 0; i < lineChart.getData().size(); i++)
                for (final XYChart.Data data : lineChart.getData().get(i).getData())
                    Tooltip.install(data.getNode(), new Tooltip("X: " + formatter.format(data.getXValue()) + " Y: " + formatter.format(data.getYValue())));

        } catch (Exception ex) {
            MyUtils.showMessage("",
                    "Error", "Por favor revisa la ayuda acerca de como ingresar la funcion", Alert.AlertType.ERROR);
        }
    }

    private void biseccionAction() {

        try {
            String def = txtFunctionMain.getText().trim();
            double pointA = Double.parseDouble(txtPointA.getText());
            double pointB = Double.parseDouble(txtPointB.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);

            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByBiseccion(pointA, pointB);
            ObservableList listValues = resolveMethod.getProcedure();
            tableViewProcedure.setItems(listValues);

        } catch (NumberFormatException e) {
            MyUtils.showMessage("Asegurate de ingresar: punto A, punto B, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void falseRuleAction() {
        try {
            String def = txtFunctionMain.getText().trim();
            double pointA = Double.parseDouble(txtPointA.getText());
            double pointB = Double.parseDouble(txtPointB.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);

            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByFalseRule(pointA, pointB);
            ObservableList listValues = resolveMethod.getProcedure();
            tableViewProcedure.setItems(listValues);

        } catch (NumberFormatException e) {
            MyUtils.showMessage("Asegurate de ingresar: punto A, punto B, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void fixedPointAction() {
        try {
            String def = txtFunctionMain.getText().trim();
            String gFunc = txtGFunction.getText().trim();
            double pointX = Double.parseDouble(txtPointAOpen.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);
            Function gFunction = new Function(gFunc);

            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByFixedPoint(gFunction, pointX);
            ObservableList listValues = resolveMethod.getProcedure();
            tableViewProcedure.setItems(listValues);

        } catch (NumberFormatException e) {
            MyUtils.showMessage("Asegurate de ingresar: Funcion g(x), punto A, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void newtonAction() {
        try {
            String def = txtFunctionMain.getText().trim();
            String dFunc = txtDerived.getText().trim();
            double pointX = Double.parseDouble(txtPointAOpen.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);
            Function dFunction = new Function(dFunc);

            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveByNewtonRaphson(dFunction, pointX);
            ObservableList listValues = resolveMethod.getProcedure();
            tableViewProcedure.setItems(listValues);

        } catch (NumberFormatException e) {
            MyUtils.showMessage("Asegurate de ingresar: Funcion g(x), punto A, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void secantAction() {
        try {
            String def = txtFunctionMain.getText().trim();
            double pointA = Double.parseDouble(txtPointA.getText());
            double pointB = Double.parseDouble(txtPointB.getText());
            double error = Double.parseDouble(txtError.getText());
            Function function = new Function(def);

            resolveMethod.setErrorPermited(error);
            resolveMethod.setFunction(function);
            resolveMethod.resolveBySecant(pointA, pointB);
            ObservableList listValues = resolveMethod.getProcedure();
            tableViewProcedure.setItems(listValues);

        } catch (NumberFormatException e) {
            MyUtils.showMessage("Asegurate de ingresar: punto A, punto B, Error", "Error", "", Alert.AlertType.WARNING);
        }
    }

    private void btnResolveAction() {
        resolveMethod.restartProcedure();

        if (typeMthods == CLOSE_METHODS) {
            if (cmbMethod.getSelectionModel().getSelectedIndex() == 0)
                biseccionAction();
            else if (cmbMethod.getSelectionModel().getSelectedIndex() == 1)
                falseRuleAction();
        } else {
            if (cmbMethod.getSelectionModel().getSelectedIndex() == 0)
                fixedPointAction();
            else if (cmbMethod.getSelectionModel().getSelectedIndex() == 1)
                newtonAction();
            else if (cmbMethod.getSelectionModel().getSelectedIndex() == 2)
                secantAction();
        }
    }

    private void cleanAll() {
        txtError.setText("");
        txtFrom.setText("");
        txtFunction.setText("");
        txtFunctionMain.setText("");
        txtTo.setText("");
        if (txtPointA != null) txtPointA.setText("");
        if (txtPointB != null) txtPointA.setText("");
        if (txtPointAOpen != null) txtPointAOpen.setText("");
        if (txtDerived != null) txtDerived.setText("");
        if (txtGFunction != null) txtGFunction.setText("");
        lineChart.getData().clear();
        tabPane.getSelectionModel().selectFirst();
        fileFunction.restartFile();
        resolveMethod.restartProcedure();
        ((Stage) txtFunctionMain.getParent().getScene().getWindow()).setTitle("Nuevo Documento");
    }

    /**
     * Guarda una funcion
     *
     * @param stage
     * @param typeSave True para Guardar. False para Guardar como...
     */
    private void mnuSaveAction(Stage stage, boolean typeSave) {
        boolean save = false;
        byte typeMethod = (byte) cmbMethod.getSelectionModel().getSelectedIndex();
        FileFunction.BeanFunction beanFunction = null;

        if (typeMthods != 0)
            typeMethod += 2;

        if (typeMethod == FileFunction.BeanFunction.BISECCION || typeMethod == FileFunction.BeanFunction.FALSE_RULE
                || typeMethod == FileFunction.BeanFunction.SECANT) {

            String f = txtFunction.getText() != null ? txtFunction.getText() : "";
            String from = txtFrom.getText() != null ? txtFrom.getText() : "";
            String to = txtTo.getText() != null ? txtTo.getText() : "";
            String a = txtPointA.getText() != null ? txtPointA.getText() : "";
            String b = txtPointB.getText() != null ? txtPointB.getText() : "";
            String e = txtError.getText() != null ? txtError.getText() : "";
            beanFunction = new FileFunction.BeanFunction(typeMethod, f, from, to, a, b, e);

        } else if (typeMethod == FileFunction.BeanFunction.PUNTO_FIJO) {

            String f = txtFunction.getText() != null ? txtFunction.getText() : "";
            String from = txtFrom.getText() != null ? txtFrom.getText() : "";
            String to = txtTo.getText() != null ? txtTo.getText() : "";
            String a = txtPointAOpen.getText() != null ? txtPointAOpen.getText() : "";
            String e = txtError.getText() != null ? txtError.getText() : "";
            String gFun = txtGFunction.getText() != null ? txtGFunction.getText() : "";
            beanFunction = new FileFunction.BeanFunction(typeMethod, f, from, to, a, e);
            beanFunction.setExtraFunction(gFun);

        } else if (typeMethod == FileFunction.BeanFunction.NEWTON) {
            String f = txtFunction.getText() != null ? txtFunction.getText() : "";
            String from = txtFrom.getText() != null ? txtFrom.getText() : "";
            String to = txtTo.getText() != null ? txtTo.getText() : "";
            String a = txtPointAOpen.getText() != null ? txtPointAOpen.getText() : "";
            String e = txtError.getText() != null ? txtError.getText() : "";
            String dFun = txtDerived.getText() != null ? txtDerived.getText() : "";
            beanFunction = new FileFunction.BeanFunction(typeMethod, f, from, to, a, e);
            beanFunction.setExtraFunction(dFun);
        }

        if (fileFunction.getFunctionFile() != null && typeSave) {
            fileFunction.openFile(fileFunction.getFunctionFile());
            save = true;
        } else {
            fileChooser.setTitle("Save As...");
            File file = refactorFileName(fileChooser.showSaveDialog(stage));
            if (file != null) {
                fileFunction.openFile(file);
                save = true;
            }
        }

        if (save) {
            fileFunction.saveFunction(beanFunction);
            fileFunction.closeFile();
            ((Stage) txtFunction.getParent().getScene().getWindow()).setTitle(fileFunction.getFunctionFile().getName());
        }
    }


    private void mnuOpenAction(Stage stage) {
        fileChooser.setTitle("Open");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {

            fileFunction.openFile(file);
            FileFunction.BeanFunction bean = fileFunction.readFunction();
            byte type = bean.getTypeMethod();

            if (typeMthods == 0) {
                if (type != FileFunction.BeanFunction.BISECCION && type != FileFunction.BeanFunction.FALSE_RULE) {
                    MyUtils.showMessage("Estas en métodos cerrados e intentas abrir un archivo " +
                            "de métodos abiertos", "Error al abrir el archivo", null, Alert.AlertType.WARNING);
                    return;
                }
            } else {
                if (type != FileFunction.BeanFunction.PUNTO_FIJO && type != FileFunction.BeanFunction.NEWTON &&
                        type != FileFunction.BeanFunction.SECANT) {
                    MyUtils.showMessage("Estas en métodos abiertos e intentas abrir un archivo " +
                            "de métodos cerrados", "Error al abrir el archivo", null, Alert.AlertType.WARNING);
                    return;
                }
            }

            txtFunction.setText(bean.getFunction());
            txtFunctionMain.setText(bean.getFunction());
            txtFrom.setText(bean.getFrom());
            txtTo.setText(bean.getTo());

            if (type == FileFunction.BeanFunction.BISECCION || type == FileFunction.BeanFunction.FALSE_RULE) {
                buildClosedPane();
                txtPointA.setText(bean.getPointA());
                txtPointB.setText(bean.getPointB());
                txtError.setText(bean.getError());

            } else if (type == FileFunction.BeanFunction.PUNTO_FIJO) {
                buildFixedPointPane();
                txtPointAOpen.setText(bean.getPointA());
                txtError.setText(bean.getError());
                txtGFunction.setText(bean.getExtraFunction());

            } else if (type == FileFunction.BeanFunction.NEWTON) {
                buildNewtonPane();
                txtPointAOpen.setText(bean.getPointA());
                txtError.setText(bean.getError());
                txtDerived.setText(bean.getExtraFunction());

            } else if (type == FileFunction.BeanFunction.SECANT) {
                buildSecantPane();
                txtPointA.setText(bean.getPointA());
                txtPointB.setText(bean.getPointB());
                txtError.setText(bean.getError());
            }

            if(typeMthods != CLOSE_METHODS)
                type -= 2;

            cmbMethod.getSelectionModel().select(type);
            fileFunction.closeFile();
            ((Stage) txtFunction.getParent().getScene().getWindow()).setTitle(file.getName());
        }
    }

    private File refactorFileName(File file) {
        File refactorFile = file;
        if (file != null)
            if (!file.getName().endsWith(".func"))
                refactorFile = new File(file.getPath() + ".func");

        return refactorFile;
    }

    private void buildClosedPane() {
        txtPointA = (TextField) paneCloseMethod.getChildren().get(1);
        txtPointB = (TextField) paneCloseMethod.getChildren().get(3);
        txtError = (TextField) paneCloseMethod.getChildren().get(5);
        paneMethod.getChildren().set(2, paneCloseMethod);
        lblMethod.setText("Métodos Cerrados");
        txtPointA.requestFocus();
    }

    private void buildFixedPointPane() {
        txtGFunction = (TextField) paneFixedPointMethod.getChildren().get(1);
        txtPointAOpen = (TextField) paneFixedPointMethod.getChildren().get(3);
        txtError = (TextField) paneFixedPointMethod.getChildren().get(5);
        paneMethod.getChildren().set(2, paneFixedPointMethod);
        lblMethod.setText("Métodos Abiertos");
        txtGFunction.requestFocus();
    }

    private void buildNewtonPane() {
        txtDerived = (TextField) paneNewtonMethod.getChildren().get(1);
        txtPointAOpen = (TextField) paneNewtonMethod.getChildren().get(3);
        txtError = (TextField) paneNewtonMethod.getChildren().get(5);
        paneMethod.getChildren().set(2, paneNewtonMethod);
        lblMethod.setText("Métodos Abiertos");
        txtDerived.requestFocus();
    }

    private void buildSecantPane() {
        txtPointA = (TextField) paneSecantMethod.getChildren().get(1);
        txtPointB = (TextField) paneSecantMethod.getChildren().get(3);
        txtError = (TextField) paneSecantMethod.getChildren().get(5);
        paneMethod.getChildren().set(2, paneSecantMethod);
        lblMethod.setText("Métodos Abiertos");
        txtPointA.requestFocus();
    }

    public void setColumnsCloseMethods(){
        TableColumn<ValuesBean, String> colValue1 = new TableColumn<ValuesBean, String>("No.");
        TableColumn<ValuesBean, String> colValue2 = new TableColumn<ValuesBean, String>("a");
        TableColumn<ValuesBean, String> colValue3 = new TableColumn<ValuesBean, String>("b");
        TableColumn<ValuesBean, String> colValue4 = new TableColumn<ValuesBean, String>("f(a)");
        TableColumn<ValuesBean, String> colValue5 = new TableColumn<ValuesBean, String>("f(b)");
        TableColumn<ValuesBean, String> colValue6 = new TableColumn<ValuesBean, String>("Xr");
        TableColumn<ValuesBean, String> colValue7 = new TableColumn<ValuesBean, String>("f(Xr)");
        TableColumn<ValuesBean, String> colValue8 = new TableColumn<ValuesBean, String>("Error");

        colValue1.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value1"));
        colValue2.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value2"));
        colValue3.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value3"));
        colValue4.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value4"));
        colValue5.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value5"));
        colValue6.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value6"));
        colValue7.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value7"));
        colValue8.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value8"));

        tableViewProcedure.getColumns().clear();
        tableViewProcedure.getColumns().addAll(colValue1, colValue2, colValue3, colValue4, colValue5, colValue6, colValue7, colValue8);

        resizeColumns();
        colValue1.setPrefWidth(45);
    }

    public void setColumnsFixedPoints(){
        TableColumn<ValuesBean, String> colValue1 = new TableColumn<ValuesBean, String>("No.");
        TableColumn<ValuesBean, String> colValue2 = new TableColumn<ValuesBean, String>("Xo");
        TableColumn<ValuesBean, String> colValue3 = new TableColumn<ValuesBean, String>("Xi");
        TableColumn<ValuesBean, String> colValue4 = new TableColumn<ValuesBean, String>("Error");

        colValue1.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value1"));
        colValue2.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value2"));
        colValue3.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value3"));
        colValue4.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value4"));

        tableViewProcedure.getColumns().clear();
        tableViewProcedure.getColumns().addAll(colValue1, colValue2, colValue3, colValue4);

        resizeColumns();
        colValue1.setPrefWidth(45);
    }

    public void setColumnsNewtonRaphson(){
        TableColumn<ValuesBean, String> colValue1 = new TableColumn<ValuesBean, String>("No.");
        TableColumn<ValuesBean, String> colValue2 = new TableColumn<ValuesBean, String>("Xi");
        TableColumn<ValuesBean, String> colValue3 = new TableColumn<ValuesBean, String>("F(Xi)");
        TableColumn<ValuesBean, String> colValue4 = new TableColumn<ValuesBean, String>("F'(xi)");
        TableColumn<ValuesBean, String> colValue5 = new TableColumn<ValuesBean, String>("Xr");
        TableColumn<ValuesBean, String> colValue6 = new TableColumn<ValuesBean, String>("Error");

        colValue1.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value1"));
        colValue2.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value2"));
        colValue3.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value3"));
        colValue4.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value4"));
        colValue5.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value5"));
        colValue6.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value6"));

        tableViewProcedure.getColumns().clear();
        tableViewProcedure.getColumns().addAll(colValue1, colValue2, colValue3, colValue4, colValue5, colValue6);

        resizeColumns();
        colValue1.setPrefWidth(45);
    }

    public void setColumnsSecant(){
        TableColumn<ValuesBean, String> colValue1 = new TableColumn<ValuesBean, String>("No.");
        TableColumn<ValuesBean, String> colValue2 = new TableColumn<ValuesBean, String>("a");
        TableColumn<ValuesBean, String> colValue3 = new TableColumn<ValuesBean, String>("b");
        TableColumn<ValuesBean, String> colValue4 = new TableColumn<ValuesBean, String>("f(a)");
        TableColumn<ValuesBean, String> colValue5 = new TableColumn<ValuesBean, String>("f(b)");
        TableColumn<ValuesBean, String> colValue6 = new TableColumn<ValuesBean, String>("Xr");
        TableColumn<ValuesBean, String> colValue7 = new TableColumn<ValuesBean, String>("Error");

        colValue1.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value1"));
        colValue2.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value2"));
        colValue3.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value3"));
        colValue4.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value4"));
        colValue5.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value5"));
        colValue6.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value6"));
        colValue7.setCellValueFactory(new PropertyValueFactory<ValuesBean, String>("value7"));

        tableViewProcedure.getColumns().clear();
        tableViewProcedure.getColumns().addAll(colValue1, colValue2, colValue3, colValue4, colValue5, colValue6, colValue7);

        resizeColumns();
        colValue1.setPrefWidth(45);
    }

    private void resizeColumns(){
        for (TableColumn t : tableViewProcedure.getColumns())
            t.setPrefWidth(105);
    }




}
