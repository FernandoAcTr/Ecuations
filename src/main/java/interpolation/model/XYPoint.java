package interpolation.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import utils.MyUtils;

public class XYPoint implements EventHandler<KeyEvent> {
    private float x;
    private float y;
    private int index;

    private TextField txtPointX;
    private TextField txtPointY;

    public XYPoint(int index, TableView tableView) {
        txtPointX = new TextField();
        txtPointY = new TextField();
        this.index = index;
        x = 0;
        y = 0;

        initTextField(tableView);
    }

    private void initTextField(final TableView tableView) {

        txtPointX.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (newValue) //focused
                    tableView.getSelectionModel().select(index);
                else {        //unfocused
                    if (txtPointX.getText().length() > 0)
                        x = Float.valueOf(txtPointX.getText());
                    else
                        txtPointX.setText("0");

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

        txtPointX.setOnKeyTyped(this);
        txtPointY.setOnKeyTyped(this);
    }

    public TextField getTxtPointX() {
        return txtPointX;
    }

    public TextField getTxtPointY() {
        return txtPointY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void handle(KeyEvent event) {
        if (Character.isLetter(event.getCharacter().charAt(0)))
            event.consume();
    }
}
