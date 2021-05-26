package home;

import animatefx.animation.FadeIn;
import bugs.reportGeneration;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class main extends Application {
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root= FXMLLoader.load(getClass().getResource("main.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
        Scene scene = new Scene(root);

        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        stage.setTitle("Bugs Life");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        new FadeIn(root).play();
    }




}
