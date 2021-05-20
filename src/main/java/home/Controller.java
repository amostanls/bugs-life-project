package home;

import bugs.MySQLOperation;
import bugs.Project;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javax.swing.*;
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
    private VBox displayBox;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    void getAddView(MouseEvent event) {

    }

    @FXML
    void overview(ActionEvent event) {

    }
    ObservableList<Project> projectList= FXCollections.observableArrayList();
    @FXML
    void refreshTable(MouseEvent event) throws Exception {
        displayBox.setVisible(true);
        label1.setVisible(true);
        label2.setVisible(true);
        updateTable();
        System.out.println("Refresh");
        displayBox.setVisible(false);
        label1.setVisible(false);
        label2.setVisible(false);
    }

    @FXML
    void search(MouseEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        project_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        project_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        project_issues.setCellValueFactory(new PropertyValueFactory<>("issuesNumber"));
        displayBox.setVisible(false);
        label1.setVisible(false);
        label2.setVisible(false);
        try {
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTable() throws Exception {

        Connection myConn = MySQLOperation.getConnection();
        projectList.clear();
        projectList=MySQLOperation.getProjectListObservable(myConn);

        projectTable.setItems(projectList);


    }


}
