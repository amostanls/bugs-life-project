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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import static bugs.MySQLOperation.myConn;
import static home.Controller.getFinalProjectList;
import static home.Controller.getSelectedID;

public class issuesController implements Initializable {
    //ObservableList<Issue> issueList = FXCollections.observableArrayList();

    private Stage stage;
    private Scene scene;
    private Parent root;

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

    private static int selectedIssue;
    @FXML
    void overview(ActionEvent event) throws IOException {

        Parent root= FXMLLoader.load(getClass().getResource("main.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToComment(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click

            selectedIssue=issueTable.getSelectionModel().getSelectedItem().getId();
            /*
            FXMLLoader loader=new FXMLLoader(getClass().getResource("comment.fxml"));
            root=loader.load();
            commentController comment_Controller=loader.getController();
            comment_Controller.updateTextField(selectedIssue);

             */
            Parent root= FXMLLoader.load(getClass().getResource("comment.fxml"));
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

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
        ObservableList<Issue> issueList = FXCollections.observableArrayList(getFinalProjectList().get(getSelectedID()-1).getIssues());
        //issueList.clear();
        //issueList= MySQLOperation.getIssueList(myConn,Controller.selectedID);
        issueTable.setItems(issueList);
    }

    public static int getSelectedIssue() {
        return selectedIssue;
    }
}
