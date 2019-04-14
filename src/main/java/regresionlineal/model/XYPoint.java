package regresionlineal.model;

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
    private float SR, ST;
    private int index;

    private TextField txtPointX;
    private TextField txtPointY;

    public XYPoint(int index, TableView tableView) {
        txtPointX = new TextField();
        txtPointY = new TextField();
        this.index = index;
        x = 0;
        y = 0;
        ST = 0;
        SR = 0;

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

    //----------------------------------metodos que son para la TableView

    public String getXY() {
        return (x * y == 0) ? "" : MyUtils.format(x * y);
    }

    public String getSquareX() {
       return (x == 0) ?  "" : MyUtils.format(x * x);
    }

    public String getSR() {
        return SR == 0 ? "" : MyUtils.format(SR);
    }

    public String getST() {
        return ST == 0 ? "" : MyUtils.format(ST);
    }

    //-----------------------------------------------

    public void setST(float Ymedia){
        ST = (float) Math.pow(y - Ymedia, 2);
    }

    public void setSR(float A0, float A1){
        SR = (float) Math.pow(y - A0 - A1*x, 2);
    }

    public float getFloatST(){
        return ST;
    }

    public float getFloatSR(){
        return SR;
    }

    public void handle(KeyEvent event) {
        if (Character.isLetter(event.getCharacter().charAt(0)))
            event.consume();
    }
}
