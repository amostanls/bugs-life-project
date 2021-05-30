package home;

import bugs.Issue;
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
    private TextArea issueDesc;

    @FXML
    private TextField issueStatus;

    @FXML
    private TextField issueImageURL;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;


    @FXML
    public void setSaveBtn(ActionEvent event) {
        String tag = issueTag.getText();
        String status = issueStatus.getText();
        String priorityString = issuePriority.getText();
        int priority = 0;
        if (!priorityString.isEmpty()) priority = Integer.valueOf(priorityString);

        String title = issueTitle.getText();
        String issueDescription = issueDesc.getText();
        String url = "";


        if (tag.isEmpty() || title.isEmpty() || issueDescription.isEmpty() || priorityString.isEmpty() || status.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
        } else if (priority > 9 || priority < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a priority between 1-9");
            alert.showAndWait();
        } else {
            //connect to database
            if (tag.equals(issue_temp.getTags()) && title.equals(issue_temp.getTitle()) && issueDescription.equals(issue_temp.getDescriptionText()) && status.equals(issue_temp.getStatus()) && priority == issue_temp.getPriority()) {
                //same,no change in data
                //System.out.println("SAME,no change");
                if (issueImageURL.getText() != null && issueImageURL.getText().length() != 0) {
                    if (Controller.isValidURL(issueImageURL.getText())) {
                        url = issueImageURL.getText();
                        updateIssue(connectionToDatabase(), getSelectedProjectId(), getSelectedIssueId(), title, priority, status, tag, issueDescription, url);
                        clean();
                        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Not a valid URL");
                        alert.showAndWait();
                    }
                }
            } else {

                updateIssue(connectionToDatabase(), getSelectedProjectId(), getSelectedIssueId(), title, priority, status, tag, issueDescription, url);
                clean();
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();


            }


        }
    }

    private void clean() {
        issueTag.clear();
        issuePriority.clear();
        issueTitle.clear();
        issueStatus.clear();
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

    Issue issue_temp = null;

    public void setTextField() {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();


        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getIssue_id() == getSelectedIssueId()) {
                issue_temp = issueList.get(i);
            }
        }

        issueStatus.setText(issue_temp.getStatus());
        issueTag.setText(issue_temp.getTags() + "");
        issuePriority.setText(issue_temp.getPriority() + "");

        issueTitle.setText(issue_temp.getTitle() + "");
        if (issue_temp.getUrl() == null) {
            issueImageURL.setText("");
        } else {
            issueImageURL.setText(issue_temp.getUrl());
        }


        issueDesc.setText(issue_temp.getDescriptionText() + "");


    }


}
