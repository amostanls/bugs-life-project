package home;

import bugs.MySQLOperation;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class issueAddController implements Initializable {

    @FXML
    private TextField issueTag;

    @FXML
    private TextField issuePriority;

    @FXML
    private TextField issueTitle;

    @FXML
    private TextField issueAssignedTo;

    @FXML
    private TextArea issueDesc;

    @FXML
    private TextField issueImageURL;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;



    @FXML
    public void setSaveBtn(ActionEvent event) {
        String tag=issueTag.getText();
        String priorityString=issuePriority.getText();
        int priority=0;
        if(!priorityString.isEmpty()) priority=Integer.valueOf(priorityString);

        String title=issueTitle.getText();
        String assignee=issueAssignedTo.getText();
        String issueDescription=issueDesc.getText();
        String url="";



        if(tag.isEmpty() ||title.isEmpty() ||assignee.isEmpty() ||issueDescription.isEmpty() || priorityString.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
        }
        else if(priority>9||priority<1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a priority between 1-9");
            alert.showAndWait();
        }
        else{
            if (issueImageURL.getText() != null && issueImageURL.getText().length() != 0) {
                if (Controller.isValidURL(issueImageURL.getText())) {
                    url=issueImageURL.getText();
                    System.out.println(url);
                    MySQLOperation.createIssueJavaFX(MySQLOperation.getConnection(),Controller.getSelectedProjectId(),Controller.getUsername(),tag,priority,title,assignee,issueDescription,url);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Not a valid URL");
                    alert.showAndWait();
                }
            }
            else{
                MySQLOperation.createIssueJavaFX(MySQLOperation.getConnection(),Controller.getSelectedProjectId(),Controller.getUsername(),tag,priority,title,assignee,issueDescription,url);
            }

            clean();
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }
    }

    private void clean(){
        issueTag.clear();
        issuePriority.clear();
        issueTitle.clear();
        issueAssignedTo.clear();
        issueDesc.clear();
    }

    @FXML
    public void setCancelBtn(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
