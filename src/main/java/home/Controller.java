package home;

import bugs.MySQLOperation;
import bugs.Project;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import static bugs.MySQLOperation.myConn;

public class Controller implements Initializable {


    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, String> project_id;

    @FXML
    private TableColumn<Project, String> project_name;

    @FXML
    private TableColumn<Project, Integer> project_issues;




    private Stage stage;
    private Scene scene;
    private Parent root;
    private static List<Project> finalProjectList;
    private static int selectedID;

    private static boolean initialise=true;
    @FXML
    void getAddView(MouseEvent event) {

    }

    @FXML
    void overview(ActionEvent event) throws IOException {
//dont need
        Parent root= FXMLLoader.load(getClass().getResource("main.fxml"));
        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    void refreshTable(MouseEvent event) throws Exception {

        updateTable();
        setProjectTable();
        System.out.println("Refresh");

    }

    @FXML
    void search(MouseEvent event) {

    }

    public void switchToIssues(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            selectedID=projectTable.getSelectionModel().getSelectedItem().getId();
            Parent root= FXMLLoader.load(getClass().getResource("issue.fxml"));
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        project_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        project_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        project_issues.setCellValueFactory(new PropertyValueFactory<>("issuesNumber"));

        try {
            if(initialise){
                updateTable();
                initialise=false;
            }

            setProjectTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTable() throws Exception {
        finalProjectList=MySQLOperation.getProjectList(myConn);
    }

    public void setProjectTable(){
        ObservableList<Project> projectList = FXCollections.observableList(finalProjectList);
        projectTable.setItems(projectList);
        //System.out.println(projectList.get(0).getIssues());
    }


    public static List<Project> getFinalProjectList() {
        return finalProjectList;
    }

    public static int getSelectedID() {
        return selectedID;
    }
}
