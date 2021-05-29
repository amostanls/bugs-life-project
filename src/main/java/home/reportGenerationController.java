package home;

import bugs.reportGeneration;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;


import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;


public class reportGenerationController implements Initializable {

    private static Service<Void> backGroundThread;

    @FXML
    private TextArea reportDisplay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Timestamp ts = Timestamp.valueOf("2019-08-07 23:44:00.0");
        reportGeneration report = new reportGeneration(ts);
        reportDisplay.setText(report.toString());
    }

   /* private void projectTableBackGroundTask() {
        backGroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Controller.updateTable();
                        return null;
                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            setProjectTable();
            searchProject();
            //JOptionPane.showMessageDialog(null, "Refresh Completed");

        });

        Region veil = new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4); -fx-background-radius: 30 30 0 0;");
        //veil.setStyle("-fx-background-radius: 30 30 0 0");
        veil.setPrefSize(1030, 530);

        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(100, 100);

        p.progressProperty().bind(backGroundThread.progressProperty());
        veil.visibleProperty().bind(backGroundThread.runningProperty());
        p.visibleProperty().bind(backGroundThread.runningProperty());

        stackPane.getChildren().addAll(veil, p);
        backGroundThread.start();
    }*/
}
