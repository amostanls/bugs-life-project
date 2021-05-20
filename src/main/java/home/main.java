package home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene=new Scene(root);


        //stage.initStyle(StageStyle.UNDECORATED);
        //String css=this.getClass().getResource("main.css").toExternalForm();
        //scene.getStylesheets().add(css);
        stage.setTitle("Bugs Life");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


}