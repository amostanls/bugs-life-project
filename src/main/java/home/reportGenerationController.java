package home;

import bugs.reportGeneration;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;


import java.net.URL;
import java.util.ResourceBundle;


public class reportGenerationController implements Initializable {

    @FXML
    private TextArea reportDisplay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reportGeneration report=new reportGeneration();
        reportDisplay.setText(report.toString());
    }
}
