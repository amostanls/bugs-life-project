package home;

import bugs.Issue;
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
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static home.Controller.getFinalProjectList;
import static home.Controller.getSelectedProjectId;


public class issuesController implements Initializable {
    ObservableList<Issue> issueList = null;

    private boolean isEditing = false;
    private boolean isChange = false;
    private static Service<Void> backGroundThread;

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
            Parent parent = FXMLLoader.load(getClass().getResource("issue_add.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(issuesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void getEditView() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("issue_edit.fxml"));
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

    void getChangeLogView() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("issue_history.fxml"));
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
        issueTableBackGroundTask();
    }

    @FXML
    void switchToComment(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            int selectedIssue=0;
            try{
                selectedIssue = issueTable.getSelectionModel().getSelectedItem().getIssue_id();
            }catch (NullPointerException e){}

            //System.out.println(selectedIssue);
            Controller.setSelectedIssueId(selectedIssue);
            if (isEditing == true) getEditView();
            else if (isChange == true) getChangeLogView();
            else Controller.switchToComment();


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
        issueId.setCellValueFactory(new PropertyValueFactory<>("id"));
        issueTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        issueStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        issueTag.setCellValueFactory(new PropertyValueFactory<>("tagAsString"));
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
                } else if (FuzzySearch.tokenSetPartialRatio(issue.getTags(), queryString) > 60 || FuzzySearch.tokenSortPartialRatio(issue.getTags(), queryString) > 60) {
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(issue.getDescriptionText(), queryString) > 60 || FuzzySearch.tokenSortPartialRatio(issue.getDescriptionText(), queryString) > 60) {
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(issue.getCreatedBy(), queryString) > 90 || FuzzySearch.tokenSortPartialRatio(issue.getCreatedBy(), queryString) > 90) {
                    return true;
                } else if (FuzzySearch.tokenSetPartialRatio(issue.getAssignee(), queryString) > 90 || FuzzySearch.tokenSortPartialRatio(issue.getAssignee(), queryString) > 90) {
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

    private void issueTableBackGroundTask(){
        backGroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Controller.updateTable();
                        return null;
                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            try {
                setIssueTable();
                searchIssues();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //searchProject();
            //JOptionPane.showMessageDialog(null, "Refresh Completed");

        });

        Region veil=new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4); -fx-background-radius: 30 30 0 0;");
        //veil.setStyle("-fx-background-radius: 30 30 0 0");
        veil.setPrefSize(1030,530);

        ProgressIndicator p =new ProgressIndicator();
        p.setMaxSize(100,100);

        p.progressProperty().bind(backGroundThread.progressProperty());
        veil.visibleProperty().bind(backGroundThread.runningProperty());
        p.visibleProperty().bind(backGroundThread.runningProperty());

        stackPane.getChildren().addAll(veil,p);
        backGroundThread.start();
    }

}
