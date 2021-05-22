package home;

import bugs.Issue;
import bugs.MySQLOperation;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static bugs.MySQLOperation.*;
import static home.Controller.*;

public class issueEditController implements Initializable {

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
    private TextField issueStatus;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;


    @FXML
    public void setSaveBtn(ActionEvent event) {
        String tag=issueTag.getText();
        String status=issueStatus.getText();
        String priorityString=issuePriority.getText();
        int priority=0;
        if(!priorityString.isEmpty()) priority=Integer.valueOf(priorityString);

        String title=issueTitle.getText();
        String assignee=issueAssignedTo.getText();
        String issueDescription=issueDesc.getText();
        if(tag.isEmpty() ||title.isEmpty() ||assignee.isEmpty() ||issueDescription.isEmpty() || priorityString.isEmpty()||status.isEmpty()){
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
            if(!tag.equals(issue_temp.getTags())) updateIssue(connectionToDatabase(),getSelectedProjectId(),getSelectedIssueId(),"tag",tag);
            if(!status.equals(issue_temp.getStatus())) updateIssue(connectionToDatabase(),getSelectedProjectId(),getSelectedIssueId(),"status",status);
            if(priority!=issue_temp.getPriority()) updateIssue(connectionToDatabase(),getSelectedProjectId(),getSelectedIssueId(),"priority",priority);
            if(!issueDescription.equals(issue_temp.getTags())) updateIssue(connectionToDatabase(),getSelectedProjectId(),getSelectedIssueId(),"tag",tag);
            if(!tag.equals(issue_temp.getTags())) updateIssue(connectionToDatabase(),getSelectedProjectId(),getSelectedIssueId(),"tag",tag);
            if(!tag.equals(issue_temp.getTags())) updateIssue(connectionToDatabase(),getSelectedProjectId(),getSelectedIssueId(),"tag",tag);

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
        setTextField();
    }
    Issue issue_temp=null;
    public void setTextField() {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();


        for(int i=0;i<issueList.size();i++){
            if(issueList.get(i).getId()==getSelectedIssueId()){
                issue_temp=issueList.get(i);
            }
        }

        issueStatus.setText(issue_temp.getStatus());
        issueTag.setText(issue_temp.getTags()+"");
        issuePriority.setText(issue_temp.getPriority()+"");

        issueTitle.setText(issue_temp.getTitle()+"");
        issueAssignedTo.setText(issue_temp.getAssignee()+"");

        issueDesc.setText(issue_temp.getDescriptionText()+"");


    }


}
