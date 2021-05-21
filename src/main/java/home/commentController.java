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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static home.Controller.*;

public class commentController implements Initializable {

    @FXML
    private TextArea issueHeader;

    @FXML
    private TextArea issueDescription;

    @FXML
    private TextArea issueComment;

    @FXML
    void refreshTable(MouseEvent event) throws Exception {
        Controller.updateTable();
        setTextField();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextField();
    }

    public void setTextField() {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();

        issueHeader.setText(issueList.get(getSelectedIssueId()-1).printHeader());
        issueDescription.setText(issueList.get(getSelectedIssueId()-1).printIssue());
        issueComment.setText(issueList.get(getSelectedIssueId()-1).printComment());


    }
}
