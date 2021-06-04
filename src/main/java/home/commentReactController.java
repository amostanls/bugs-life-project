package home;

import bugs.Comment;
import bugs.Issue;
import bugs.MySQLOperation;
import bugs.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
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
        String reacted = check_reacted(event);
        if(reacted!=null&&reacted.equals("Happy")) {
            Stage currentStage=((Stage)(((ImageView)event.getSource()).getScene().getWindow()));
            //currentStage.close();
            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            //((Stage) (((ImageView) event.getSource()).getScene().getWindow())).close();
            //JOptionPane.showMessageDialog(null, "Reacted Happy to comment "+getSelectedCommentId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Removed 'Happy'");
            alert.showAndWait();
            return;
        }
        MySQLOperation.reacting(getCurrentUser().getUserid(),getSelectedProjectId(),getSelectedIssueId(),getSelectedCommentId(),"Happy");
        Stage currentStage=((Stage)(((ImageView)event.getSource()).getScene().getWindow()));
        //currentStage.close();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        //((Stage) (((ImageView) event.getSource()).getScene().getWindow())).close();
        //JOptionPane.showMessageDialog(null, "Reacted Happy to comment "+getSelectedCommentId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Reacted Happy to comment "+getSelectedCommentId());
        alert.showAndWait();

    }

    @FXML
    void setAngryBtn(MouseEvent event) throws Exception {
        String reacted=check_reacted(event);
        if(reacted!=null&&reacted.equals("Angry")) {
            Stage currentStage=((Stage)(((ImageView)event.getSource()).getScene().getWindow()));
            //currentStage.close();
            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            //((Stage) (((ImageView) event.getSource()).getScene().getWindow())).close();
            //JOptionPane.showMessageDialog(null, "Reacted Happy to comment "+getSelectedCommentId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Removed 'Angry'");
            alert.showAndWait();
            return;
        }
        MySQLOperation.reacting(getCurrentUser().getUserid(),getSelectedProjectId(),getSelectedIssueId(),getSelectedCommentId(), "Angry");
        Stage currentStage=((Stage)(((ImageView)event.getSource()).getScene().getWindow()));
        //currentStage.close();
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        //((Stage) (((ImageView) event.getSource()).getScene().getWindow())).close();
        //JOptionPane.showMessageDialog(null, "Reacted Angry to comment "+getSelectedCommentId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Reacted Angry to comment "+getSelectedCommentId());
        alert.showAndWait();
    }

    String check_reacted(MouseEvent event)throws Exception {
        //check if the user has reacted before
        User user = getCurrentUser();
        int userid = user.getUserid();
        int projid = getSelectedProjectId();
        int issueid = getSelectedIssueId();
        int commentid = getSelectedCommentId();
        String reacted = MySQLOperation.getReaction(MySQLOperation.getConnection(), userid, projid, issueid, commentid);
        if(reacted!=null) {
            //reacted, prompt the user a window to notify the user this comment has been reacted
            //react the same to remove
            Stage currentStage=((Stage)(((ImageView)event.getSource()).getScene().getWindow()));
            //currentStage.close();
            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("You have reacted "+commentid+" with "+reacted+"\nReact again to remove.");
            alert.showAndWait();
            MySQLOperation.delreacting(userid, projid, issueid, commentid, reacted);
        }
        return reacted;
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
        setSelectedCommentId(selection_id+1);
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
