package home;

import bugs.reportGeneration;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;


import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;


public class reportGenerationController implements Initializable {

    @FXML
    private TextArea reportDisplay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Timestamp ts = Timestamp.valueOf("2019-08-07 23:44:00.0");
        reportGeneration report = new reportGeneration(ts);
        reportDisplay.setText(report.toString());
    }
}
