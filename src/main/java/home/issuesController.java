package home;

import bugs.Issue;
import bugs.MySQLOperation;
import bugs.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static home.Controller.getFinalProjectList;
import static home.Controller.getSelectedProjectId;


public class issuesController implements Initializable {
    //ObservableList<Issue> issueList = FXCollections.observableArrayList();

    @FXML
    private TableView<Issue> issueTable;

    @FXML
    private TableColumn<Issue, Integer> issueId;

    @FXML
    private TableColumn<Issue, String> issueTitle;

    @FXML
    private TableColumn<Issue, String> issueStatus;

    @FXML
    private TableColumn<Issue, String> issueTag;

    @FXML
    private TableColumn<Issue, Integer> issuePriority;

    @FXML
    private TableColumn<Issue, Timestamp> issueTime;

    @FXML
    private TableColumn<Issue, String> issueAssignee;

    @FXML
    private TableColumn<Issue, String> issueCreatedBy;

    @FXML
    private ImageView addComment;

    @FXML
    void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("issue_add.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(issuesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    void refreshTable(MouseEvent event) throws Exception {
        Controller.updateTable();
        setIssueTable();
    }


    public void switchToComment(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            int selectedIssue=issueTable.getSelectionModel().getSelectedItem().getId();
            System.out.println(selectedIssue);
            Controller.setSelectedIssueId(selectedIssue);
            Controller.switchToComment();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        issueId.setCellValueFactory(new PropertyValueFactory<>("id"));
        issueTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        issueStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        issueTag.setCellValueFactory(new PropertyValueFactory<>("tagAsString"));
        issuePriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        issueTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        issueAssignee.setCellValueFactory(new PropertyValueFactory<>("assignee"));
        issueCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        try {
            setIssueTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIssueTable() throws Exception{
        ObservableList<Issue> issueList = FXCollections.observableArrayList(getFinalProjectList().get(getSelectedProjectId()-1).getIssues());
        issueTable.setItems(issueList);
    }

}
