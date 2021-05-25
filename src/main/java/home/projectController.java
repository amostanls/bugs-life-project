package home;

import bugs.MySQLOperation;
import bugs.Project;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.xdrop.fuzzywuzzy.FuzzySearch;

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

import static home.Controller.getFinalProjectList;

//import static bugs.MySQLOperation.myConn;


public class projectController implements Initializable {

    private boolean isEditing = false;

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

    @FXML
    private TextField searchBox;


    private static boolean initialise = true;

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

    void getEditView() {
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
        JOptionPane.showMessageDialog(null, "Refresh Completed");
        setProjectTable();
    }

    @FXML
    void search(MouseEvent event) {

    }

    public void switchToIssues(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            int selectedID = projectTable.getSelectionModel().getSelectedItem().getId();
            Controller.setSelectedProjectId(selectedID);
            if (isEditing == false) Controller.switchToIssues();
            else getEditView();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isEditToggle.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (isEditToggle.isSelected() == true) {
                    isEditing = true;
                } else {
                    isEditing = false;
                }
            }
        });
        project_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        project_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        project_issues.setCellValueFactory(new PropertyValueFactory<>("issuesNumber"));

        try {
            if (initialise) {
                Controller.updateTable();
                initialise = false;
            }
            setProjectTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchProject();
    }

    public void setProjectTable() {
        if (getFinalProjectList() != null) {
            ObservableList<Project> projectList = FXCollections.observableList(getFinalProjectList());
            projectTable.setItems(projectList);
        }

        //System.out.println(projectList.get(0).getIssues());
    }

    public static void setInitialise(boolean initialise) {
        projectController.initialise = initialise;
    }

    public void searchProject() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Project> filteredData = new FilteredList<Project>(FXCollections.observableList(getFinalProjectList()), b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(project -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) return true;

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(project.getId()).indexOf(lowerCaseFilter) != -1) {
                    System.out.println();
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(project.getName(), lowerCaseFilter) > 60 || FuzzySearch.tokenSortPartialRatio(project.getName(), lowerCaseFilter) > 60) {
                    return true;
                } else if (String.valueOf(project.getIssues().size()).indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Project> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(projectTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        projectTable.setItems(sortedData);

    }
}
