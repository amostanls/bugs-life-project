package home;

import bugs.MySQLOperation;
import bugs.Project;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, String> project_id;

    @FXML
    private TableColumn<Project, String> project_name;

    @FXML
    private TableColumn<Project, Integer> project_issues;

    @FXML
    void getAddView(MouseEvent event) {

    }

    @FXML
    void overview(ActionEvent event) {

    }
    ObservableList<Project> projectList= FXCollections.observableArrayList();
    @FXML
    void refreshTable(MouseEvent event) throws SQLException {
        System.out.println("starting");
        projectList.clear();
        projectList=MySQLOperation.getProjectList();
        projectTable.setItems(projectList);
        System.out.println("here");
    }

    @FXML
    void search(MouseEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        project_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        project_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        project_issues.setCellValueFactory(new PropertyValueFactory<>("issuesNumber"));
    }


}
