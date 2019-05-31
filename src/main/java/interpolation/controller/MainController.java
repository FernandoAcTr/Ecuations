package interpolation.controller;

import com.jfoenix.controls.JFXComboBox;
import interpolation.model.InterpolationPoint;
import interpolation.model.XYPoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import utils.MyUtils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable, EventHandler<KeyEvent> {
    @FXML
    private MenuItem mnuNew;

    @FXML
    private MenuItem mnuHowFillData;

    @FXML
    private MenuItem mnuHowSolve;

    @FXML
    private MenuItem mnuAbout;

    @FXML
    private JFXComboBox<String> cmbType;

    @FXML
    private Button btnAddRow;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TableView<XYPoint> tablePoints;

    @FXML
    private TableView<InterpolationPoint> tableInterpolation;

    private final int LINEAR_INTERPOLATION = 0;
    private final int SQUARE_INTERPOLATION = 1;
    private final int DIFFERENCE_INTERPOLATION = 2;

    private ObservableList<XYPoint> listPoints;
    private int currentType = LINEAR_INTERPOLATION;
    int grade = 3; //solo se usa para diferencias divididas y lagrange

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
        tablePoints.setDisable(true);
        tablePoints.setFocusTraversable(false);
        tableInterpolation.setDisable(true);
        tableInterpolation.setFocusTraversable(false);

        mnuNew.setOnAction(event -> clearAll());

        mnuHowFillData.setOnAction(event -> {
            String help = "Usted debe seleccionar, mediante el combo box el tipo de interpolcicón que" +
                    "\ndesea aplicar. La tabla generará el número de puntos que el método requiere, mismos que" +
                    "\n usted debe llenar con información.";
            MyUtils.showHelpMessage(help, 600, 200);
        });

        mnuHowSolve.setOnAction(event -> {
            String help = "Cuando tenga la tabla debidamente llena con información" +
                    "\nllene ingrese el valor para el cual desea interpolar, en la" +
                    "\nsiguiente tabla y posteriormente de click en \"Interpolar\"." +
                    "\nSi deses interpolar para más valores, puede agregar más filas" +
                    "\ndando click en el botón \"+\"";
            MyUtils.showHelpMessage(help, 570, 160);
        });

        cmbType.getItems().addAll("Interpolación Lineal", "Interpolación Cuadrática",
                "Interpolación por Diferencias Divididas");

        cmbType.valueProperty().addListener((observable, oldValue, newValue) -> {
            tableInterpolation.setDisable(false);
            tablePoints.setDisable(false);
            changeNumRows();
        });

        btnAddRow.setOnAction(event -> addInterpolationRow());

        addTablePointsColumns();
        addTableInterpolColumns();
        addInterpolationRow();
    }

    private void addTablePointsColumns() {

        TableColumn<XYPoint, TextField> columnX = new TableColumn<>("X");
        TableColumn<XYPoint, TextField> columnY = new TableColumn<>("Y");

        columnX.setCellValueFactory(new PropertyValueFactory<>("txtPointX"));
        columnY.setCellValueFactory(new PropertyValueFactory<>("txtPointY"));

        tablePoints.getColumns().addAll(columnX, columnY);

        for (TableColumn c : tablePoints.getColumns()) {
            c.setSortable(false);
            c.setPrefWidth(230);
        }
    }

    private void addTableInterpolColumns() {

        TableColumn<InterpolationPoint, TextField> column1 = new TableColumn<>("Interpolar para:");
        TableColumn<InterpolationPoint, TextField> column2 = new TableColumn<>("Interpolación");
        TableColumn<InterpolationPoint, Button> columnButton = new TableColumn<>();

        column1.setCellValueFactory(new PropertyValueFactory<>("txtValueFor"));
        column2.setCellValueFactory(new PropertyValueFactory<>("txtInterpolation"));
        columnButton.setCellValueFactory(new PropertyValueFactory<>("btnInterpolate"));

        tableInterpolation.getColumns().addAll(column1, column2, columnButton);

        for (TableColumn c : tableInterpolation.getColumns()) {
            c.setSortable(false);
            c.setPrefWidth(230);
        }

        columnButton.setPrefWidth(100);
    }

    /**
     * Genera las filas de la tabla vacias
     */
    private void generateRows(int numRows) {
        listPoints.clear();
        for (int i = 0; i < numRows; i++)
            listPoints.add(new XYPoint(i, tablePoints));

        tablePoints.setDisable(false);
    }

    private void addInterpolationRow(){
        int index = tableInterpolation.getItems().size();
        tableInterpolation.getItems().add(new InterpolationPoint(index, tableInterpolation, listPoints, currentType, grade));
    }

    private void changeNumRows(){
        switch (cmbType.getSelectionModel().getSelectedIndex()){
            case LINEAR_INTERPOLATION:
                currentType = LINEAR_INTERPOLATION;
                generateRows(2);
                break;

            case SQUARE_INTERPOLATION:
                currentType = SQUARE_INTERPOLATION;
                generateRows(3);
                break;

            case DIFFERENCE_INTERPOLATION:
                currentType = DIFFERENCE_INTERPOLATION;
                grade = showDialogGrade(3);
                generateRows(grade+1);
        }

        tableInterpolation.getItems().clear();
        addInterpolationRow();
    }

    private void clearAll() {
        tablePoints.getItems().clear();
        tableInterpolation.getItems().clear();
        cmbType.getSelectionModel().clearSelection();
        tablePoints.setDisable(true);
        tableInterpolation.setDisable(true);
    }

    public void handle(KeyEvent event) {
        if (Character.isLetter(event.getCharacter().charAt(0)))
            event.consume();
    }

    private int showDialogGrade(int defaultGrade) {
        final Dialog<Integer> dialog = new Dialog<Integer>();
        dialog.setTitle("Grado");
        dialog.setContentText("Selecciona el grado del método");

        final Spinner<Integer> spinner = new Spinner();
        final ButtonType buttonOk = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);

        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10));
        spinner.getValueFactory().setValue(defaultGrade);
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
            return result.get();

        return defaultGrade;
    }
}
