package home;

import bugs.Comment;
import bugs.Issue;
import bugs.MySQLOperation;
import bugs.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static home.Controller.*;
import static home.Controller.getSelectedIssueId;

public class commentReactController implements Initializable {

    ArrayList<Comment> possible_comments = new ArrayList<>();
    ArrayList<String> comment_list = new ArrayList<>();

    int selection_id;

    @FXML
    private ChoiceBox<String> commentSelection;

    @FXML
    private TextArea commentField;

    @FXML
    void setHappyBtn(MouseEvent event) throws Exception {
        String hasReacted = check_reacted();
        if (hasReacted != null && hasReacted.equals("happy")) {//ask user whether want to remove it?
            promptUserToRemoveReaction("happy", event);
        } else {
            MySQLOperation.reacting(getCurrentUser().getUserid(), getSelectedProjectId(), getSelectedIssueId(), getSelectedCommentId(), "happy");
            Stage currentStage = ((Stage) (((ImageView) event.getSource()).getScene().getWindow()));
            //currentStage.close();
            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            //((Stage) (((ImageView) event.getSource()).getScene().getWindow())).close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Reacted Happy to comment " + getSelectedCommentId());
            alert.showAndWait();
        }

    }

    @FXML
    void setAngryBtn(MouseEvent event) throws Exception {
        String hasReacted = check_reacted();
        if (hasReacted != null && hasReacted.equals("angry")) {//ask user whether want to remove it?
            promptUserToRemoveReaction("angry", event);
        } else {
            MySQLOperation.reacting(getCurrentUser().getUserid(), getSelectedProjectId(), getSelectedIssueId(), getSelectedCommentId(), "angry");
            Stage currentStage = ((Stage) (((ImageView) event.getSource()).getScene().getWindow()));
            //currentStage.close();
            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            //((Stage) (((ImageView) event.getSource()).getScene().getWindow())).close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Reacted Angry to comment " + getSelectedCommentId());
            alert.showAndWait();
        }
    }

    @FXML
    void setThumbsUpBtn(MouseEvent event) throws Exception {
        String hasReacted = check_reacted();
        if (hasReacted != null && hasReacted.equals("thumbsup")) {//ask user whether want to remove it?
            promptUserToRemoveReaction("thumbsup", event);
        } else {
            MySQLOperation.reacting(getCurrentUser().getUserid(), getSelectedProjectId(), getSelectedIssueId(), getSelectedCommentId(), "thumbsup");
            Stage currentStage = ((Stage) (((ImageView) event.getSource()).getScene().getWindow()));
            //currentStage.close();
            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            //((Stage) (((ImageView) event.getSource()).getScene().getWindow())).close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Reacted Thumbs Up to comment " + getSelectedCommentId());
            alert.showAndWait();
        }
    }


    String check_reacted() {
        //check if the user has reacted before
        String reacted = MySQLOperation.getReaction(MySQLOperation.getConnection(), getCurrentUser().getUserid(), getSelectedProjectId(), getSelectedIssueId(), getSelectedCommentId());//this line might take some time
        if (reacted != null) {
            return reacted;//has previous reaction
        } else {
            return null;//no previous reaction
        }
    }

    void promptUserToRemoveReaction(String reaction, MouseEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("You have reacted " + getSelectedCommentId() + " with " + reaction.toUpperCase());
        alert.setContentText("Do you want to remove it?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            MySQLOperation.delreacting(getCurrentUser().getUserid(), getSelectedProjectId(), getSelectedIssueId(), getSelectedCommentId(), reaction);
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setHeaderText(null);
            alert2.setContentText("Removed " + reaction.toUpperCase());
            alert2.showAndWait();
            Stage currentStage = ((Stage) (((ImageView) event.getSource()).getScene().getWindow()));
            //currentStage.close();
            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCommentSelection();
        commentSelection.getItems().addAll(comment_list);
        commentSelection.setValue(comment_list.get(0));
        commentField.setText(possible_comments.get(0).getText());
        setSelectedCommentId(1);
        commentSelection.setOnAction(this::getCommentSelection);
    }

    private void clean() {
        commentField.clear();
    }

    private void getCommentSelection(ActionEvent actionEvent) {
        selection_id = commentSelection.getSelectionModel().getSelectedIndex();
        setSelectedCommentId(selection_id + 1);
        //System.out.println(getSelectedCommentId());
        commentField.setText(possible_comments.get(selection_id).getText());
    }

    Issue issue_temp = null;

    public void setCommentSelection() {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();
        //Issue issue_temp = getFinalProjectList().get(getSelectedProjectId()-1).getIssues().get(getSelectedIssueId()-1);

        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getIssue_id() == getSelectedIssueId()) {
                issue_temp = issueList.get(i);
            }
        }
        for (int i = 0; i < issue_temp.getComments().size(); i++) {

            possible_comments.add(issue_temp.getComments().get(i));
            String temp_string = issue_temp.getComments().get(i).getText();
            if (temp_string.length() > 20)
                comment_list.add(issue_temp.getComments().get(i).getComment_id() + " - " + issue_temp.getComments().get(i).getText().substring(0, 20));
            else
                comment_list.add(issue_temp.getComments().get(i).getComment_id() + " - " + issue_temp.getComments().get(i).getText());

        }
    }
}
