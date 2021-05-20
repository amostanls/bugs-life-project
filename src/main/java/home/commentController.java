package home;

import bugs.Issue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static home.Controller.getFinalProjectList;
import static home.Controller.getSelectedID;
import static home.issuesController.getSelectedIssue;

public class commentController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextArea issueHeader;

    @FXML
    private TextArea issueDescription;

    @FXML
    private TextArea issueComment;
    @FXML
    void overview(ActionEvent event) throws IOException {

        Parent root= FXMLLoader.load(getClass().getResource("main.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextField();
    }

    public void setTextField() {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedID() - 1).getIssues();

        issueHeader.setText(issueList.get(getSelectedIssue()-1).printHeader());
        issueDescription.setText(issueList.get(getSelectedIssue()-1).printIssue());
        issueComment.setText(issueList.get(getSelectedIssue()-1).printComment());


    }
}
