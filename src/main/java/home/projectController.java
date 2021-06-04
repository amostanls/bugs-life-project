package home;

import bugs.Project;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static home.Controller.getFinalProjectList;

//import static bugs.MySQLOperation.myConn;


public class projectController implements Initializable {

    private boolean isEditing = false;
    private boolean isChange = false;
    private static Service<Void> backGroundThread;

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, String> project_id;

    @FXML
    private TableColumn<Project, String> project_name;

    @FXML
    private TableColumn<Project, Integer> project_issues;

    @FXML
    private ToggleGroup tgProject;

    @FXML
    private JFXToggleButton isEditToggle;

    @FXML
    private JFXToggleButton changeLogToggle;

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
            stage.setOnCloseRequest(windowEvent -> projectTableBackGroundTask());
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
            stage.setOnCloseRequest(windowEvent -> projectTableBackGroundTask());
        } catch (IOException ex) {
            Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void getChangeLogView() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("project_history.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            stage.setOnCloseRequest(windowEvent -> projectTableBackGroundTask());

        } catch (IOException ex) {
            Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void refreshTable(MouseEvent event) throws Exception {
        projectTableBackGroundTask();
    }

    @FXML
    void search(MouseEvent event) {

    }

    public void switchToIssues(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            int selectedID = 0;
            if (projectTable.getSelectionModel().getSelectedItem() != null) {
                try {
                    selectedID = projectTable.getSelectionModel().getSelectedItem().getProject_id();
                } catch (NullPointerException e) {
                }

                Controller.setSelectedProjectId(selectedID);
                if (isEditing == true) getEditView();
                else if (isChange == true) getChangeLogView();
                else Controller.switchToIssues();
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        isEditToggle.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (isEditToggle.isSelected() == true) isEditing = true;
            else isEditing = false;
        });

        changeLogToggle.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (changeLogToggle.isSelected() == true) isChange = true;
            else isChange = false;
        });

        project_id.setCellValueFactory(new PropertyValueFactory<>("project_id"));
        project_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        project_issues.setCellValueFactory(new PropertyValueFactory<>("issuesNumber"));
        if (getFinalProjectList() != null) searchProject();
        if (initialise) {
            //projectTableBackGroundTask();
            initialise = false;
        }
    }

    public void setProjectTable() {
        if (getFinalProjectList() != null) {
            ObservableList<Project> projectList = FXCollections.observableList(getFinalProjectList());
            projectTable.setItems(projectList);
        }
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

                if (String.valueOf(project.getProject_id()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(project.getName(), lowerCaseFilter) > 60 || FuzzySearch.tokenSortPartialRatio(project.getName(), lowerCaseFilter) > 60) {
                    return true;
                } else if (String.valueOf(project.getIssues().size()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false; // Does not match.
                }

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

    private void projectTableBackGroundTask() {
        backGroundThread = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        Controller.updateTable();
                        return null;
                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            setProjectTable();
            searchProject();
            //JOptionPane.showMessageDialog(null, "Refresh Completed");

        });

        Region veil = new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4); -fx-background-radius: 30 30 0 0;");
        //veil.setStyle("-fx-background-radius: 30 30 0 0");
        veil.setPrefSize(1030, 530);

        //JFXSpinner p=new JFXSpinner();

        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(100, 100);

        p.progressProperty().bind(backGroundThread.progressProperty());
        veil.visibleProperty().bind(backGroundThread.runningProperty());
        p.visibleProperty().bind(backGroundThread.runningProperty());

        stackPane.getChildren().addAll(veil, p);
        backGroundThread.start();
    }

}
