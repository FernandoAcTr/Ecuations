import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.MyUtils;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("/common_res/main_window1.fxml"));

        Parent root = FXMLLoader.load(getClass().getResource("/common_res/main_window2.fxml"));
        MyUtils.undecorateWindow(primaryStage, root, true);

        Scene scene = new Scene(root, 730,600);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        scene.getStylesheets().add("/css/jfoenix-design.css");
        scene.getStylesheets().add("/css/jfoenix-fonts.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
