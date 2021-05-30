package home;

import bugs.Issue;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.*;

import static bugs.MySQLOperation.*;
import static home.Controller.*;

public class issueEditController implements Initializable {

//    @FXML
//    private TextField issueTag;
//
//    @FXML
//    private TextField issuePriority;

    @FXML
    private TextField issueTitle;

    @FXML
    private TextArea issueDesc;

//    @FXML
//    private TextField issueStatus;

    @FXML
    private TextField issueImageURL;
    @FXML
    private CheckComboBox<String> issueTag;

    @FXML
    private ComboBox<String> issuePriority;

    @FXML
    private ComboBox<String> issueStatus;

    @FXML
    private TextField issueAssignee;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;

    private ArrayList<String> list = new ArrayList<>();

    @FXML
    void setAddTag(MouseEvent event) {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Add Tag");
        td.getDialogPane().setHeaderText(null);
        td.getDialogPane().setContentText("Enter your new tag : ");
        td.showAndWait();
        TextField input = td.getEditor();
        if (input.getText() != null && input.getText().toString().length() != 0) {
            issueTag.getItems().addAll(input.getText());
        }
    }


    @FXML
    public void setSaveBtn(ActionEvent event) {
        List list = issueTag.getCheckModel().getCheckedItems();
        //String tag = issueTag.getText();

        String tag = "";
        if (list.isEmpty()) {
            tag = "";
        } else {
            for (Object obj : list) {
                tag += obj.toString().replaceAll("\\s+", "") + " ";//removes all white spaces character
            }
        }

        String status = issueStatus.getValue();

        String priorityString = issuePriority.getValue();
        int priority = 0;
        if (!priorityString.isEmpty()) priority = Integer.valueOf(priorityString);

        String title = issueTitle.getText().trim();
        String assignee = issueAssignee.getText().trim();
        String issueDescription = issueDesc.getText();
        String url = issueImageURL.getText();


        if (title.isEmpty() || issueDescription.isEmpty() || priorityString.isEmpty() || status.isEmpty()) {
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
            if (Objects.equals(tag, issue_temp.getTags()) && title.equals(issue_temp.getTitle()) && issueDescription.equals(issue_temp.getDescriptionText()) && status.equals(issue_temp.getStatus()) && priority == issue_temp.getPriority() && Objects.equals(url, issue_temp.getUrl()) && Objects.equals(assignee,issue_temp.getAssignee())) {

                System.out.println("SAME,no change");//same,no change in data
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();


            } else {//exists some changes

                if (issueImageURL.getText() != null && issueImageURL.getText().length() != 0) {//url is not empty
                    if (Controller.isValidURL(issueImageURL.getText())) {//url is valid

                        url = issueImageURL.getText();
                        updateIssue(getConnection(), getSelectedProjectId(), getSelectedIssueId(), title, priority, status, tag, issueDescription, assignee, url);
                        System.out.println("HAVE URL");
                        clean();
                        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Not a valid URL");
                        alert.showAndWait();
                    }
                } else {//url is empty

                    url = null;
                    updateIssue(getConnection(), getSelectedProjectId(), getSelectedIssueId(), title, priority, status, tag, issueDescription, assignee, url);
                    System.out.println("NO URL");
                    clean();
                    ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
                }


            }


        }
    }

    private void clean() {
        //issueTag.clear();
        //issuePriority.clear();
        issueTitle.clear();
        //issueStatus.clear();
        issueDesc.clear();
    }

    @FXML
    public void setCancelBtn(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();


        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getIssue_id() == getSelectedIssueId()) {
                issue_temp = issueList.get(i);
            }
        }
        String[] statusList = {"Open", "Closed", "In Progress", "Resolved", "Reopened"};
        String[] priorityList = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        List<String> tagsList = new ArrayList<String>(Arrays.asList(issue_temp.getTags().split(" ")));

        issueStatus.getItems().addAll(statusList);
        issuePriority.getItems().addAll(priorityList);
        issueTag.getItems().addAll(tagsList);
//        for(int i=0;i<tagsList.size();i++){
//            issueTag.getItems().add(tagsList.get(i).substring(1,tagsList.get(i).length()-2));
//        }


        for (int i = 0; i < 4; i++) {
            if (issue_temp.getStatus().equals(statusList[i])) {
                issueStatus.getSelectionModel().select(i);
            }
        }

        issuePriority.getSelectionModel().select(Integer.valueOf(issue_temp.getPriority()) - 1);

        for (int i = 0; i < tagsList.size(); i++) {
            issueTag.getCheckModel().check(i);
        }

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

//        issueStatus.setText(issue_temp.getStatus());
//        issueTag.setText(issue_temp.getTags() + "");
//        issuePriority.setText(issue_temp.getPriority() + "");

        issueTitle.setText(issue_temp.getTitle() + "");
        if (issue_temp.getUrl() == null) {
            issueImageURL.setText("");
        } else {
            issueImageURL.setText(issue_temp.getUrl());
        }


        issueDesc.setText(issue_temp.getDescriptionText() + "");
        issueAssignee.setText(issue_temp.getAssignee() + " ");
        issueImageURL.setText(issue_temp.getUrl());

    }


}
