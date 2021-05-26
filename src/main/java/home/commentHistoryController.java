package home;

import bugs.Comment_History;
import bugs.Issue_History;
import bugs.MySQLOperation;
import bugs.User;
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

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static home.Controller.*;

public class commentHistoryController implements Initializable {

    @FXML
    private TableView<Comment_History> commentTable;

    @FXML
    private TableColumn<Comment_History, String> commentVersion;

    @FXML
    private TableColumn<Comment_History, String> commentId;

    @FXML
    private TableColumn<Comment_History, String> commentText;

    @FXML
    private TableColumn<Comment_History, String> commentTimestamp;

    @FXML
    private TableColumn<Comment_History, String> commentUser;

    @FXML
    void changeToVersion(MouseEvent event) {
        if (event.getClickCount() == 2) {//Checking double click
            Comment_History comment = commentTable.getSelectionModel().getSelectedItem();

            MySQLOperation.updateComment(MySQLOperation.connectionToDatabase(), Controller.getCurrentUser(), getSelectedProjectId(), getSelectedIssueId(), comment.getComment_id(), comment.getText());
            ((Stage) (((TableView) event.getSource()).getScene().getWindow())).close();


        }
    }

    public void setCommentTable() {

        ObservableList<Comment_History> commentHistory = FXCollections.observableArrayList(MySQLOperation.getCommentHistoryList(MySQLOperation.connectionToDatabase(), getSelectedProjectId(), getSelectedIssueId(), getSelectedCommentId()));
        ObservableList<Comment_History> commentHistoryForSpecificUser = FXCollections.observableArrayList();
        for (int i = 0; i < commentHistory.size(); i++) {
            if (commentHistory.get(i).getUser().equals(Controller.getUsername())) {
                commentHistoryForSpecificUser.add(commentHistory.get(i));
            }
        }
        commentTable.setItems(commentHistory);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commentTable.setPlaceholder(new Label(("Your comments currently have no changes")));
        commentVersion.setCellValueFactory(new PropertyValueFactory<>("version_id"));
        commentId.setCellValueFactory(new PropertyValueFactory<>("comment_id"));
        commentText.setCellValueFactory(new PropertyValueFactory<>("text"));
        commentTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        commentUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        setCommentTable();
    }
}

