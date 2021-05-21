package home;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class projectEditController implements Initializable {
    @FXML
    private TextField projectIdField;

    @FXML
    private TextField projectNameField;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    public void setSaveBtn(ActionEvent event) {
        String name=projectNameField.getText();
        if(name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
        }
        else{
            System.out.println(Controller.getSelectedProjectId()+" "+name);
            clean();
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }
    }

    private void clean(){
        projectIdField.clear();
        projectNameField.clear();
    }

    @FXML
    public void setCancelBtn(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        projectIdField.setText(Controller.getSelectedProjectId()+"");
    }
}