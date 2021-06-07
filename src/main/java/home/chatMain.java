package home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class chatMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Configure windows
        Parent root = FXMLLoader.load(getClass().getResource("tempPage.fxml"));
        primaryStage.setTitle("temp");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
