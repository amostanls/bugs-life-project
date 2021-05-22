package home;

import bugs.Issue;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static home.Controller.*;

public class commentController implements Initializable {

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
    private JFXToggleButton isEditToggle;


    @FXML
    void refreshTable(MouseEvent event) throws Exception {
        Controller.updateTable();
        JOptionPane.showMessageDialog(null,"Refresh Completed");
        setTextField();
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isEditable(false);
        isEditToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(isEditToggle.isSelected()==true){
                    isEditable(true);
                }else{
                    isEditable(false);
                }
            }
        });
        setTextField();
    }




    public void isEditable(boolean b){
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
        Issue issue_temp=null;
        for(int i=0;i<issueList.size();i++){
            if(issueList.get(i).getId()==getSelectedIssueId()){
                issue_temp=issueList.get(i);
            }
        }
        issueID.setText(issue_temp.getId()+"");
        issueStatus.setText(issue_temp.getStatus());
        issueTag.setText(issue_temp.getTags()+"");
        issuePriority.setText(issue_temp.getPriority()+"");
        issueCreatedOn.setText(issue_temp.getTimestamp()+"");
        issueTitle.setText(issue_temp.getTitle()+"");
        issueAssignedTo.setText(issue_temp.getAssignee()+"");
        issueCreatedBy.setText(issue_temp.getCreatedBy()+"");
        issueDesc.setText(issue_temp.getDescriptionText()+"");
        issueComment.setText(issue_temp.printComment());
    /*
        issueHeader.setText(issueList.get(getSelectedIssueId()-1).printHeader());
        issueDescription.setText(issueList.get(getSelectedIssueId()-1).printIssue());
        issueComment.setText(issueList.get(getSelectedIssueId()-1).printComment());


     */

    }
}
