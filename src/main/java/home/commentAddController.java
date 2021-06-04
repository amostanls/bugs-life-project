package home;

import bugs.MySQLOperation;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static home.Controller.*;

public class commentAddController implements Initializable {

    @FXML
    private TextArea commentField;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    void setCancelBtn(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    void setSaveBtn(ActionEvent event) {
        String comment=commentField.getText();
        if(comment.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
        }
        else{
            //System.out.println(Controller.getSelectedProjectId()+" "+name);
            MySQLOperation.createComment(MySQLOperation.getConnection(),getSelectedProjectId(),getSelectedIssueId(),getUsername(),comment);
            clean();
            //((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
            Stage currentStage=((Stage)(((Button)event.getSource()).getScene().getWindow()));
            //currentStage.close();
            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    private void clean(){

        commentField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
