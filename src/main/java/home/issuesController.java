package home;

import bugs.Issue;
import bugs.MySQLOperation;
import bugs.Project;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static home.Controller.getFinalProjectList;
import static home.Controller.getSelectedProjectId;


public class issuesController implements Initializable {
    ObservableList<Issue> issueList = null;

    private boolean isEditing = false;
    private boolean isChange = false;
    private static Service<ArrayList<Issue>> backGroundThread;

    @FXML
    private StackPane stackPane;


    @FXML
    private TextField issueSearch;

    @FXML
    private TableView<Issue> issueTable;

    @FXML
    private TableColumn<Issue, Integer> issueId;

    @FXML
    private TableColumn<Issue, String> issueTitle;

    @FXML
    private TableColumn<Issue, String> issueStatus;

    @FXML
    private TableColumn<Issue, String> issueTag;

    @FXML
    private TableColumn<Issue, Integer> issuePriority;

    @FXML
    private TableColumn<Issue, Timestamp> issueTime;

    @FXML
    private TableColumn<Issue, String> issueAssignee;

    @FXML
    private TableColumn<Issue, String> issueCreatedBy;

    @FXML
    private ImageView addComment;

    @FXML
    private JFXToggleButton isEditToggle;

    @FXML
    private ToggleGroup tgIssue;

    @FXML
    private JFXToggleButton changeLogToggle;

    @FXML
    void getAddView(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("issue_add.fxml"));
            Parent parent = loader.load();

            issueAddController issueAdd = loader.getController();
            issueAdd.setIssueController(this);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            //stage.setOnCloseRequest(windowEvent -> issueTableBackGroundTask());
        } catch (IOException ex) {
            Logger.getLogger(issuesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void getEditView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("issue_edit.fxml"));
            Parent parent = loader.load();

            issueEditController issueEdit = loader.getController();
            issueEdit.setIssueController(this);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            //stage.setOnCloseRequest(windowEvent -> issueTableBackGroundTask());
        } catch (IOException ex) {
            Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void getChangeLogView() {
        try {
            //Parent parent = FXMLLoader.load(getClass().getResource("issue_history.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("issue_history.fxml"));
            Parent parent = loader.load();

            issueHistoryController issueHistory = loader.getController();
            issueHistory.setIssueController(this);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            //stage.setOnCloseRequest(windowEvent -> issueTableBackGroundTask());
        } catch (IOException ex) {
            Logger.getLogger(projectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    void refreshTable(MouseEvent event) throws Exception {
        issueTableBackGroundTask();
    }

    @FXML
    void switchToComment(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            int selectedIssue = 0;
            if (issueTable.getSelectionModel().getSelectedItem() != null) {
                selectedIssue = issueTable.getSelectionModel().getSelectedItem().getIssue_id();
                //System.out.println(selectedIssue);
                Controller.setSelectedIssueId(selectedIssue);
                if (isEditing == true) getEditView();
                else if (isChange == true) getChangeLogView();
                else Controller.switchToComment();

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        isEditToggle.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (isEditToggle.isSelected()) isEditing = true;
            else isEditing = false;
        });

        changeLogToggle.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (changeLogToggle.isSelected()) isChange = true;
            else isChange = false;
        });
        issueId.setCellValueFactory(new PropertyValueFactory<>("issue_id"));
        issueTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        issueStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        issueTag.setCellValueFactory(new PropertyValueFactory<>("tags"));
        issuePriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        issueTime.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        issueAssignee.setCellValueFactory(new PropertyValueFactory<>("assignee"));
        issueCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        try {
            setIssueTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchIssues();
    }

    public void setIssueTable() throws Exception {
        issueList = FXCollections.observableArrayList(getFinalProjectList().get(getSelectedProjectId() - 1).getIssues());
        issueTable.setItems(issueList);
    }

    public void searchIssues() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Issue> filteredData = new FilteredList<>(issueList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        issueSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(issue -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) return true;

                // Compare first name and last name of every person with filter text.
                String queryString = newValue.toString();

                if (String.valueOf(issue.getIssue_id()).indexOf(queryString) != -1) {
                    System.out.println();
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(issue.getTitle(), queryString) > 60 || FuzzySearch.tokenSortPartialRatio(issue.getTitle(), queryString) > 60) {
                    return true;
                } else if (String.valueOf(issue.getPriority()).indexOf(queryString) != -1)
                    return true;
                else if (FuzzySearch.tokenSetPartialRatio(issue.getStatus(), queryString) > 60 || FuzzySearch.tokenSortPartialRatio(issue.getStatus(), queryString) > 60) {
                    return true;
                } else if (issue.getTags() != null && (FuzzySearch.tokenSetPartialRatio(issue.getTags(), queryString) > 60 || FuzzySearch.tokenSortPartialRatio(issue.getTags(), queryString) > 60)) {
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(issue.getDescriptionText(), queryString) > 60 || FuzzySearch.tokenSortPartialRatio(issue.getDescriptionText(), queryString) > 60) {
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(issue.getCreatedBy(), queryString) > 90 || FuzzySearch.tokenSortPartialRatio(issue.getCreatedBy(), queryString) > 90) {
                    return true;
                } else if (issue.getAssignee() != null && (FuzzySearch.tokenSetPartialRatio(issue.getAssignee(), queryString) > 90 || FuzzySearch.tokenSortPartialRatio(issue.getAssignee(), queryString) > 90)) {
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(issue.commentsAsString(), queryString) > 60 || FuzzySearch.tokenSortPartialRatio(issue.commentsAsString(), queryString) > 60) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Issue> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(issueTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        issueTable.setItems(sortedData);

    }

    public void issueTableBackGroundTask() {
        backGroundThread = new Service<>() {
            @Override
            protected Task<ArrayList<Issue>> createTask() {
                return new Task<>() {
                    @Override
                    protected ArrayList<Issue> call() throws Exception {
                        return MySQLOperation.getIssueListByPriority(MySQLOperation.getConnection(), getSelectedProjectId());
//                        Controller.updateTable();
//                        return null;
                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            try {
                List<Project> temp_project_list = getFinalProjectList();
                temp_project_list.get(getSelectedProjectId() - 1).setIssues(backGroundThread.getValue());
                setIssueTable();
                searchIssues();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //searchProject();
            //JOptionPane.showMessageDialog(null, "Refresh Completed");

        });

        Region veil = new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4); -fx-background-radius: 30 30 0 0;");
        //veil.setStyle("-fx-background-radius: 30 30 0 0");
        veil.setPrefSize(1030, 530);

        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(100, 100);

        p.progressProperty().bind(backGroundThread.progressProperty());
        veil.visibleProperty().bind(backGroundThread.runningProperty());
        p.visibleProperty().bind(backGroundThread.runningProperty());

        stackPane.getChildren().addAll(veil, p);
        backGroundThread.start();
    }

}
