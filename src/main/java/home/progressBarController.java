package home;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class progressBarController implements Initializable {

    BigDecimal progress=new BigDecimal(String.format("%.2f",0.0));

    @FXML
    private ProgressBar myProgressBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        while(progress.doubleValue()<1){
            progress=new BigDecimal(String.format("%.2f",progress.doubleValue()+0.1));
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myProgressBar.setProgress(progress.doubleValue());
        }
    }

}

