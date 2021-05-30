package home;

import bugs.Issue_History;
import bugs.MySQLOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static home.Controller.getSelectedIssueId;
import static home.Controller.getSelectedProjectId;

public class issueHistoryController implements Initializable {

    @FXML
    private TableView<Issue_History> issueTable;

    @FXML
    private TableColumn<Issue_History, Integer> issueVersion;

    @FXML
    private TableColumn<Issue_History, Integer> issueId;

    @FXML
    private TableColumn<Issue_History, String> issueTitle;

    @FXML
    private TableColumn<Issue_History, String> issueStatus;

    @FXML
    private TableColumn<Issue_History, String> issueTag;

    @FXML
    private TableColumn<Issue_History, String> issuePriority;

    @FXML
    private TableColumn<Issue_History, String> issueTime;

    @FXML
    private TableColumn<Issue_History, String> issueAssignee;

    @FXML
    private TableColumn<Issue_History, String> issueCreatedBy;


    @FXML
    private TableColumn<Issue_History, String> issueImageURL;

    @FXML
    void changeToVersion(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            Issue_History issue = issueTable.getSelectionModel().getSelectedItem();

            MySQLOperation.updateIssue(MySQLOperation.getConnection(), issue.getProject_id(), issue.getIssue_id(), issue.getTitle(), issue.getPriority(), issue.getStatus(), issue.getTag()[0], issue.getDescriptionText(),issue.getAssignee(),issue.getUrl());
            ((Stage) (((TableView) event.getSource()).getScene().getWindow())).close();


        }
    }

    public void setIssueHistoryTable() {

        ObservableList<Issue_History> issueHistory = FXCollections.observableArrayList(MySQLOperation.getIssueHistoryList(MySQLOperation.getConnection(), getSelectedProjectId(), getSelectedIssueId()));
        issueTable.setItems(issueHistory);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        issueTable.setPlaceholder(new Label(("This issue has no changes")));
        issueVersion.setCellValueFactory(new PropertyValueFactory<>("version_id"));
        issueId.setCellValueFactory(new PropertyValueFactory<>("issue_id"));
        issueTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        issueStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        issueTag.setCellValueFactory(new PropertyValueFactory<>("tags"));
        issuePriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        issueTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        issueAssignee.setCellValueFactory(new PropertyValueFactory<>("assignee"));
        issueCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        issueImageURL.setCellValueFactory(new PropertyValueFactory<>("url"));
        setIssueHistoryTable();

    }
}
