package multiplyregresion.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class MultiplePoint implements EventHandler<KeyEvent> {

    private TextField txtPointX1;
    private TextField txtPointX2;
    private TextField txtPointY;
    private int index;

    float x1, x2, y;

    public MultiplePoint(int index, TableView tableView) {
        this.index = index;

        txtPointX1 = new TextField();
        txtPointX2 = new TextField();
        txtPointY = new TextField();

        initTextField(tableView);
    }

    private void initTextField(final TableView tableView) {

        txtPointX1.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) //focused
                    tableView.getSelectionModel().select(index);
                else {        //unfocused
                    if (txtPointX1.getText().length() > 0)
                        x1 = Float.valueOf(txtPointX1.getText());
                    else
                        txtPointX1.setText("0");

                    tableView.refresh();
                }

            }
        });

        txtPointX2.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) //focused
                    tableView.getSelectionModel().select(index);
                else {        //unfocused
                    if (txtPointX2.getText().length() > 0)
                        x2 = Float.valueOf(txtPointX2.getText());
                    else
                        txtPointX2.setText("0");

                    tableView.refresh();
                }

            }
        });

        txtPointY.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) //focused
                    tableView.getSelectionModel().select(index);
                else {        //unfocused
                    if (txtPointY.getText().length() > 0)
                        y = Float.valueOf(txtPointY.getText());
                    else
                        txtPointY.setText("0");

                    tableView.refresh();
                }

            }
        });

        txtPointX1.setOnKeyTyped(this);
        txtPointX2.setOnKeyTyped(this);
        txtPointY.setOnKeyTyped(this);

    }

   // <editor-fold defaultstate="collapsed" desc=" getters() and setters() ">

    public TextField getTxtPointX1() {
        return txtPointX1;
    }

    public void setTxtPointX1(TextField txtPointX1) {
        this.txtPointX1 = txtPointX1;
    }

    public TextField getTxtPointX2() {
        return txtPointX2;
    }

    public void setTxtPointX2(TextField txtPointX2) {
        this.txtPointX2 = txtPointX2;
    }

    public TextField getTxtPointY() {
        return txtPointY;
    }

    public void setTxtPointY(TextField txtPointY) {
        this.txtPointY = txtPointY;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    // </editor-fold>

    public void handle(KeyEvent event) {
        if (Character.isLetter(event.getCharacter().charAt(0)))
            event.consume();
    }
}
