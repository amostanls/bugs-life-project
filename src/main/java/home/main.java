package home;

import animatefx.animation.FadeIn;
import bugs.MyRunnable;
import bugs.reportGeneration;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static chat.ChatServer.acceptClients;
import static chat.ChatServer.getPortNumber;

public class main extends Application {
    public static MyRunnable myRunnable = new MyRunnable();
    public static Thread t = new Thread(myRunnable);

    private static Service<Void> backGroundThread;

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

        stage.setOnShowing(event -> {
            t.start();
        });
        stage.show();
        chatBackGroundTask();

        new FadeIn(root).play();


    }

    private void chatBackGroundTask() {
        backGroundThread = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            ServerSocket ss = new ServerSocket(getPortNumber());
                            acceptClients(ss);
                        } catch (BindException e) {
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }
        };

        backGroundThread.start();
    }


}
