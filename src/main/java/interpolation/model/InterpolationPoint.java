package interpolation.model;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import utils.MyUtils;

import java.util.List;

public class InterpolationPoint implements EventHandler<KeyEvent> {
    private TextField txtValueFor;
    private TextField txtInterpolation;
    private Button btnInterpolate;

    private final int LINEAR_INTERPOLATION = 0;
    private final int SQUARE_INTERPOLATION = 1;
    private final int DIFFERENCE_INTERPOLATION = 2;
    private final int LAGRANGE_INTERPOLATION = 3;

    private int index, typeInterpolation, grade;
    private float x, y;
    private List<XYPoint> listPoints;

    public InterpolationPoint(int index, TableView tableView, List<XYPoint> listPoints, int typeInterpolation, int grade) {
        txtValueFor = new TextField();
        txtValueFor.setOnKeyTyped(this);

        txtInterpolation = new TextField();
        txtInterpolation.setEditable(false);
        txtInterpolation.setFocusTraversable(false);
        btnInterpolate = new Button("Interpolar");
        btnInterpolate.setOnAction(event -> interpolate());

        this.index = index;
        this.typeInterpolation = typeInterpolation;
        this.listPoints = listPoints;
        this.grade = grade;
        initTextField(tableView);
    }

    private void initTextField(final TableView tableView) {

        txtValueFor.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { //focused
                tableView.getSelectionModel().select(index);
                txtValueFor.setStyle(null);
            } else {        //unfocused
                if (txtValueFor.getText().length() > 0)
                    x = Float.valueOf(txtValueFor.getText());

                tableView.refresh();
            }

        });

        txtValueFor.setOnKeyTyped(this);
        txtInterpolation.setOnKeyTyped(this);
    }

    public void interpolate() {
        if (txtValueFor.getText().length() == 0)
            txtValueFor.setStyle("-fx-border-color: red; -fx-background-color: rgba(255, 0, 0, 0.2)");
        else if (Float.parseFloat(txtValueFor.getText()) < listPoints.get(0).getX() ||
                Float.parseFloat(txtValueFor.getText()) > listPoints.get(listPoints.size() - 1).getX()) {
            MyUtils.showMessage("El punto no esta contenido en el intervalo de puntos", "Error", null, Alert.AlertType.WARNING);
            txtValueFor.setStyle("-fx-border-color: red; -fx-background-color: rgba(255, 0, 0, 0.2)");
        } else {
            Interpolator interpolator = new Interpolator();
            float v;
            switch (typeInterpolation) {

                case LINEAR_INTERPOLATION:
                    v = interpolator.getLinearInterpolation(listPoints.get(0), listPoints.get(1), Float.valueOf(txtValueFor.getText()));
                    txtInterpolation.setText(MyUtils.format(v));
                    break;

                case SQUARE_INTERPOLATION:
                    v = interpolator.getSquareInterpolation(listPoints.get(0), listPoints.get(1), listPoints.get(2), Float.valueOf(txtValueFor.getText()));
                    txtInterpolation.setText(MyUtils.format(v));
                    break;

                case DIFFERENCE_INTERPOLATION:
                    XYPoint[] points = new XYPoint[listPoints.size()];
                    for (int i = 0, j = listPoints.size() - 1; i < listPoints.size(); i++, j--)
                        points[i] = listPoints.get(j);

                    v = interpolator.getDiffInterpolation(points, grade, Float.valueOf(txtValueFor.getText()));
                    txtInterpolation.setText(MyUtils.format(v));
                    break;

                case LAGRANGE_INTERPOLATION:
                    XYPoint[] points2 = new XYPoint[listPoints.size()];
                    for (int i = 0; i < listPoints.size(); i++)
                        points2[i] = listPoints.get(i);

                    v = interpolator.getLagrangeInterpolation(points2, Float.valueOf(txtValueFor.getText()));
                    txtInterpolation.setText(MyUtils.format(v));
            }
        }
    }

    public void handle(KeyEvent event) {
        if (Character.isLetter(event.getCharacter().charAt(0)))
            event.consume();
    }

    // <editor-fold defaultstate="collapsed" desc=" getters() & setters() ">

    public TextField getTxtValueFor() {
        return txtValueFor;
    }

    public void setTxtValueFor(TextField txtValueFor) {
        this.txtValueFor = txtValueFor;
    }

    public TextField getTxtInterpolation() {
        return txtInterpolation;
    }

    public void setTxtInterpolation(TextField txtInterpolation) {
        this.txtInterpolation = txtInterpolation;
    }

    public Button getBtnInterpolate() {
        return btnInterpolate;
    }

    public void setBtnInterpolate(Button btnInterpolate) {
        this.btnInterpolate = btnInterpolate;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    // </editor-fold>
}
