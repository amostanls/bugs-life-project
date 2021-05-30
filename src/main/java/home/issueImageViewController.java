package home;

import bugs.Issue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static home.Controller.*;

public class issueImageViewController implements Initializable {

    @FXML
    private AnchorPane imagePlaceholder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Issue> issueList = getFinalProjectList().get(getSelectedProjectId() - 1).getIssues();
        //Issue issue_temp = getFinalProjectList().get(getSelectedProjectId()-1).getIssues().get(getSelectedIssueId()-1);
        Issue issue_temp = null;
        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getIssue_id() == getSelectedIssueId()) {
                issue_temp = issueList.get(i);
            }
        }

        if (issue_temp.getUrl() == null || issue_temp.getUrl().length() == 0) {//no image provided
            Label label = new Label("No Image");
            imagePlaceholder.getChildren().add(label);
        } else if (!Controller.isValidURL(issue_temp.getUrl())) {
            Label label = new Label("Invalid URL\nUnable to display image");
            imagePlaceholder.getChildren().add(label);
        } else {
            ImageView image = new ImageView(issue_temp.getUrl());
            imagePlaceholder.getChildren().add(image);
        }
    }
}
