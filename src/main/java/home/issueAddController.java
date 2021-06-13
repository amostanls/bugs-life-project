package home;

import bugs.MySQLOperation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class issueAddController implements Initializable {

    private issuesController ic;
    @FXML
    private ComboBox<String> issuePriority;

    @FXML
    private CheckComboBox<String> issueTag;

    @FXML
    private TextField issueTitle;

    @FXML
    private TextField issueAssignedTo;

    @FXML
    private TextArea issueDesc;

    @FXML
    private TextField issueImageURL;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton cancelBtn;

    private ArrayList<String> list = new ArrayList<>();


    @FXML
    void setAddTag(MouseEvent event) {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Add Tag");
        td.getDialogPane().setHeaderText(null);
        td.getDialogPane().setContentText("Enter your new tag : ");
        td.showAndWait();
        TextField input = td.getEditor();
        if (input.getText() != null && input.getText().toString().length() != 0) {
            list.add(input.getText());
            issueTag.getItems().clear();
            issueTag.getItems().addAll(list);

        }
    }

    @FXML
    public void setSaveBtn(ActionEvent event) throws IOException {
        //String tag=issueTag.getText();
        List list = issueTag.getCheckModel().getCheckedItems();
        String tag = "";
        if (list.isEmpty()) {
            tag = "";
        } else {
            for (Object obj : list) {
                tag += obj.toString().replaceAll("\\s+", "") + " ";//removes all white spaces character
            }
        }
        tag=tag.trim();

        String priorityString = issuePriority.getValue();
        int priority = 0;
        if(priorityString!=null){
            if (!priorityString.isEmpty()) priority = Integer.valueOf(priorityString);
        }


        String title = issueTitle.getText().trim();
        String assignee = issueAssignedTo.getText();
        String issueDescription = issueDesc.getText();
        String url = null;


        if (title.isEmpty() || issueDescription.isEmpty() || priorityString==null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in priority, title, and issue description");
            alert.showAndWait();
        } else if (priority > 9 || priority < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please enter a priority between 1-9");
            alert.showAndWait();
        } else {

            if (issueImageURL.getText() != null && issueImageURL.getText().length() != 0) {
                if (Controller.isValidURL(issueImageURL.getText())) {
                    url = issueImageURL.getText();
                    //System.out.println(url);
                    MySQLOperation.createIssueJavaFX(MySQLOperation.getConnection(), Controller.getSelectedProjectId(), Controller.getUsername(), tag, priority, title, assignee, issueDescription, url);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Not a valid URL");
                    alert.showAndWait();
                }
            } else {
                MySQLOperation.createIssueJavaFX(MySQLOperation.getConnection(), Controller.getSelectedProjectId(), Controller.getUsername(), tag, priority, title, assignee, issueDescription, url);
            }

            clean();
            Stage currentStage = ((Stage) (((Button) event.getSource()).getScene().getWindow()));
            currentStage.close();
            ic.issueTableBackGroundTask();
//            Stage currentStage = ((Stage) (((Button) event.getSource()).getScene().getWindow()));
//            //currentStage.close();
//            currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }

    private void clean() {
        issueTitle.clear();
        issueAssignedTo.clear();
        issueDesc.clear();
    }

    @FXML
    public void setCancelBtn(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.add("Frontend");
        list.add("Backend");
        list.add("Java");
        list.add("Windows");

        issueTag.getItems().addAll(list);

        issuePriority.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9");
        issuePriority.setPromptText("Select priority");

    }


    public void setIssueController(issuesController issueController) {
        this.ic = issueController;
    }
}
