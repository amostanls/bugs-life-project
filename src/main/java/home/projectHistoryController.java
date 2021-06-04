package home;

import bugs.MySQLOperation;
import bugs.Project_History;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static home.Controller.*;

public class projectHistoryController implements Initializable {

    @FXML
    private TableView<Project_History> projectTable;

    @FXML
    private TableColumn<Project_History, Integer> project_id;

    @FXML
    private TableColumn<Project_History, String> project_name;

    @FXML
    private TableColumn<Project_History, String> project_version;

    @FXML
    private TableColumn<Project_History, String> project_timestamp;

    @FXML
    void changeToVersion(MouseEvent event) {
        if (event.getClickCount() == 2) {//Checking double click

            //update project history
            if(projectTable.getSelectionModel().getSelectedItem()!=null){
                MySQLOperation.updateProject(MySQLOperation.getConnection(), Controller.getSelectedProjectId(), projectTable.getSelectionModel().getSelectedItem().getName());
                Stage currentStage=((Stage)(((TableView)event.getSource()).getScene().getWindow()));
                //currentStage.close();
                currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        }
    }

    public void setProjectTable() {

        ObservableList<Project_History> projectHistory = FXCollections.observableList(MySQLOperation.getProjectHistoryList(MySQLOperation.getConnection(), getSelectedProjectId()));
        projectTable.setItems(projectHistory);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        projectTable.setPlaceholder(new Label("This project has no changes"));
        project_id.setCellValueFactory(new PropertyValueFactory<>("project_id"));
        project_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        project_version.setCellValueFactory(new PropertyValueFactory<>("version_id"));
        project_timestamp.setCellValueFactory(new PropertyValueFactory<>("originalTime"));
        setProjectTable();
    }
}