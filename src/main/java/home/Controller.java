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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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

//import static bugs.MySQLOperation.myConn;

public class Controller implements Initializable {

    private static int selectedProjectId=0;
    private static int selectedIssueId=0;
    private static int selectedCommentId=0;
    private static List<Project> finalProjectList;


    @FXML
    private BorderPane mainPane;


    @FXML
    void overview(ActionEvent event) throws IOException {
        Pane view=new FxmlLoader().getPage("dashboard");
        mainPane.setCenter(view);
    }

    @FXML
    void search(MouseEvent event) {

    }

    void switchToIssues() throws IOException {
        Pane view=new FxmlLoader().getPage("issue");
        mainPane.setCenter(view);
    }

    void switchToComment(){
        Pane view=new FxmlLoader().getPage("comment");
        mainPane.setCenter(view);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pane view=new FxmlLoader().getPage("dashboard");
        mainPane.setCenter(view);
    }

    public static void updateTable() throws Exception {
//        setFinalProjectList(MySQLOperation.getProjectList(myConn));
    }

    public static int getSelectedProjectId() {
        return selectedProjectId;
    }

    public static void setSelectedProjectId(int selectedProjectId) {
        Controller.selectedProjectId = selectedProjectId;
    }

    public static int getSelectedIssueId() {
        return selectedIssueId;
    }

    public static void setSelectedIssueId(int selectedIssueId) {
        Controller.selectedIssueId = selectedIssueId;
    }

    public static int getSelectedCommentId() {
        return selectedCommentId;
    }

    public static void setSelectedCommentId(int selectedCommentId) {
        Controller.selectedCommentId = selectedCommentId;
    }

    public static List<Project> getFinalProjectList() {
        return finalProjectList;
    }

    public static void setFinalProjectList(List<Project> finalProjectList) {
        Controller.finalProjectList = finalProjectList;
    }
}
