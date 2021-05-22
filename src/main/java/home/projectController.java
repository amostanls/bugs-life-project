package home;

import bugs.MySQLOperation;
import bugs.Project;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

//import static bugs.MySQLOperation.myConn;


public class projectController implements Initializable  {

    private boolean isEditing=false;

    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, String> project_id;

    @FXML
    private TableColumn<Project, String> project_name;

    @FXML
    private TableColumn<Project, Integer> project_issues;

    @FXML
    private JFXToggleButton isEditToggle;


    private static boolean initialise=true;

    @FXML
    void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("project_add.fxml"));
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

    void getEditView(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("project_edit.fxml"));
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

    @FXML
    void refreshTable(MouseEvent event) throws Exception {
        Controller.updateTable();
        JOptionPane.showMessageDialog(null,"Refresh Completed");
        setProjectTable();
    }

    @FXML
    void search(MouseEvent event) {

    }

    public void switchToIssues(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            int selectedID=projectTable.getSelectionModel().getSelectedItem().getId();
            Controller.setSelectedProjectId(selectedID);
            if(isEditing==false)Controller.switchToIssues();
            else getEditView();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isEditToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(isEditToggle.isSelected()==true){
                    isEditing=true;
                }else{
                    isEditing=false;
                }
            }
        });
        project_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        project_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        project_issues.setCellValueFactory(new PropertyValueFactory<>("issuesNumber"));

        try {
            if(initialise){
                Controller.updateTable();
                initialise=false;
            }
            setProjectTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProjectTable(){
        if (Controller.getFinalProjectList() != null) {
            ObservableList<Project> projectList = FXCollections.observableList(Controller.getFinalProjectList());
            projectTable.setItems(projectList);
        }

        //System.out.println(projectList.get(0).getIssues());
    }

    public static void setInitialise(boolean initialise) {
        projectController.initialise = initialise;
    }
}
