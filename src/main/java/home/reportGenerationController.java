package home;

import bugs.reportGeneration;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;


import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class reportGenerationController implements Initializable {

    private static Service<String> backGroundThread;


    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<String> reportType;

    @FXML
    private StackPane stackPane;

    @FXML
    private TextArea reportDisplay;

    @FXML
    void setGenerate(MouseEvent event) {
        String type=reportType.getValue();
        LocalDate localdate=datePicker.getValue();

        String date= localdate +" 00:00:00";

        System.out.println(date);
        //String date = "2019-08-08 00:00:00";   // user type date
        Timestamp timestamp = Timestamp.valueOf(date);
        reportBackGroundTask(timestamp,type);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String[] list = {"Weekly", "Monthly"};
        reportType.getItems().addAll(list);
        reportType.setValue(list[0]);


        //System.out.println(reportGeneration.toString());


    }

   private void reportBackGroundTask(Timestamp timestamp, String occurrence) {

        backGroundThread = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        reportGeneration report = new reportGeneration(timestamp, occurrence);  // type Weekly or Monthly
                        return report.toString();

                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            reportDisplay.setText(backGroundThread.getValue());

        });

        Region veil = new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4)");
        //veil.setStyle("-fx-background-radius: 30 30 0 0");
        veil.setPrefSize(1030, 530);

        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(100, 100);

        Label label=new Label("This might take a while\n Please wait...");
        label. setFont(new Font("Arial", 30));
        label.setPadding(new Insets(20));

        p.progressProperty().bind(backGroundThread.progressProperty());
        veil.visibleProperty().bind(backGroundThread.runningProperty());
        p.visibleProperty().bind(backGroundThread.runningProperty());
        label.visibleProperty().bind(backGroundThread.runningProperty());

        stackPane.setAlignment(label,Pos.TOP_CENTER);
        //stackPane.getChildren().add(label);

        stackPane.getChildren().addAll(veil, label,p);
        backGroundThread.start();
    }
}
