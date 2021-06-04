package home;

import bugs.Issue;
import com.jfoenix.controls.JFXButton;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static home.Controller.*;

public class commentController implements Initializable {

    private static Service<Void> backGroundThread;

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField issueID;

    @FXML
    private TextField issueTag;

    @FXML
    private TextField issuePriority;

    @FXML
    private TextField issueCreatedOn;

    @FXML
    private TextField issueTitle;

    @FXML
    private TextField issueAssignedTo;

    @FXML
    private TextField issueCreatedBy;

    @FXML
    private TextArea issueDesc;

    @FXML
    private TextArea issueComment;


    @FXML
    private TextField issueStatus;


    @FXML
    private ImageView addComment;

    @FXML
    private JFXButton editBtn;

    @FXML
    void setAddComment(MouseEvent event) throws Exception {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("comment_add.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setOnCloseRequest(windowEvent -> commentBackGroundTask());
        } catch (IOException ex) {
            Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void setEdit(MouseEvent event) throws Exception {
        if (havePreviousComment()) {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("comment_edit.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                //stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                stage.setOnCloseRequest(windowEvent -> commentBackGroundTask());
            } catch (IOException ex) {
                Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Edit Comment");
            alert.setContentText("You do not have any previous comment(s)\nYou can only edit comment(s) written by you");
            alert.showAndWait();
        }

    }


    @FXML
    void refreshTable(MouseEvent event) throws Exception {
        commentBackGroundTask();
    }

    @FXML
    void commentReact(MouseEvent event) throws Exception {
        if (haveComment()) {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("comment_react.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                //stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                stage.setOnCloseRequest(windowEvent -> commentBackGroundTask());
            } catch (IOException ex) {
                Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("React to comment");
            alert.setContentText("Currently no comment existed");
            alert.showAndWait();
        }
    }

    @FXML
    void changeLogForComment(MouseEvent event) throws Exception {
        if (haveComment()) {
            try {
                Parent parent = FXMLLoader.load(getClass().getResource("comment_history.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                //stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                stage.setOnCloseRequest(windowEvent -> commentBackGroundTask());
            } catch (IOException ex) {
                Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Change Log");
            alert.setContentText("Currently no comment existed");
            alert.showAndWait();
        }
    }
    @FXML
    void imageView(MouseEvent event) throws Exception {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("issueImageView.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isEditable(false);

        setTextField();
    }


    public void isEditable(boolean b) {
        issueID.setEditable(b);
        issueTag.setEditable(b);
        issueStatus.setEditable(b);
        issuePriority.setEditable(b);
        issueCreatedOn.setEditable(b);
        issueTitle.setEditable(b);
        issueAssignedTo.setEditable(b);
        issueCreatedBy.setEditable(b);
        issueDesc.setEditable(b);
        issueComment.setEditable(b);
    }

    public void setTextField() {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();
        //Issue issue_temp = getFinalProjectList().get(getSelectedProjectId()-1).getIssues().get(getSelectedIssueId()-1);
        Issue issue_temp = null;
        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getIssue_id() == getSelectedIssueId()) {
                issue_temp = issueList.get(i);
            }
        }
        issueID.setText(issue_temp.getIssue_id() + "");
        issueStatus.setText(issue_temp.getStatus());
        issueTag.setText(issue_temp.getTags() + "");
        issuePriority.setText(issue_temp.getPriority() + "");
        issueCreatedOn.setText(issue_temp.getTimestamp() + "");
        issueTitle.setText(issue_temp.getTitle() + "");
        issueAssignedTo.setText(issue_temp.getAssignee() + "");
        issueCreatedBy.setText(issue_temp.getCreatedBy() + "");


        issueDesc.setText(issue_temp.getDescriptionText() + "");

        issueComment.setText(issue_temp.printComment());



    }

    public boolean havePreviousComment() {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();
        Issue issue_temp = null;
        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getIssue_id() == getSelectedIssueId()) issue_temp = issueList.get(i);
        }
        for (int i = 0; i < issue_temp.getComments().size(); i++) {
            if (issue_temp.getComments().get(i).getUser().equals(Controller.getUsername())) return true;
        }
        return false;
    }

    public boolean haveComment() {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();
        Issue issue_temp = null;
        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getIssue_id() == getSelectedIssueId()) issue_temp = issueList.get(i);
        }
        if(issue_temp.getComments().size()>0) return true;
        return false;
    }

    private void commentBackGroundTask(){
        backGroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Controller.updateTable();
                        return null;
                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            try {
                setTextField();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Region veil=new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4);");
        //veil.setStyle("-fx-background-radius: 30 30 0 0");
        veil.setPrefSize(1030,590);

        ProgressIndicator p =new ProgressIndicator();
        p.setMaxSize(100,100);

        p.progressProperty().bind(backGroundThread.progressProperty());
        veil.visibleProperty().bind(backGroundThread.runningProperty());
        p.visibleProperty().bind(backGroundThread.runningProperty());

        stackPane.getChildren().addAll(veil,p);
        backGroundThread.start();
    }
}
