package home;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInLeft;
import animatefx.animation.FadeInRight;
import bugs.MySQLOperation;
import bugs.Project;
import bugs.User;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller implements Initializable {

    private static int selectedProjectId = 0;
    private static int selectedIssueId = 0;
    private static int selectedCommentId = 0;
    private static List<Project> finalProjectList;
    private static User currentUser;
    private static String username = "";

    @FXML
    private Label usernameDisplay;

    @FXML
    private BorderPane mainPane;

    public static BorderPane staticBorderPane;


    @FXML
    void overview(ActionEvent event) throws IOException {
        Pane view = new FxmlLoader().getPage("dashboard");
        mainPane.setCenter(view);
        new FadeInRight(view).play();
    }

    @FXML
    void search(MouseEvent event) {
        Pane view = new FxmlLoader().getPage("search");
        mainPane.setCenter(view);
        new FadeInRight(view).play();
    }

    @FXML
    void changeLog(MouseEvent event) {
        Pane view = new FxmlLoader().getPage("changeLog");
        mainPane.setCenter(view);
        new FadeInRight(view).play();
    }

    @FXML
    void settings(MouseEvent event) {
        Pane view = new FxmlLoader().getPage("settings");
        mainPane.setCenter(view);
        new FadeInRight(view).play();

    }

    @FXML
    void signOut(MouseEvent event) throws IOException {

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure you want to logout?");

        if(alert.showAndWait().get()== ButtonType.OK){
            resetID();
            /*Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Bugs Life");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();*/
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }

    }


    static void switchToIssues() throws IOException {
        Pane view = new FxmlLoader().getPage("issue");
        staticBorderPane.setCenter(view);
        new FadeInRight(view).setSpeed(2).play();
    }

    static void switchToComment() {
        Pane view = new FxmlLoader().getPage("comment");
        staticBorderPane.setCenter(view);
        new FadeInRight(view).setSpeed(2).play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameDisplay.setText(getUsername().toUpperCase());
        staticBorderPane = mainPane;
        Pane view = new FxmlLoader().getPage("dashboard");
        mainPane.setCenter(view);
    }


    public static void updateTable() throws Exception {

        setFinalProjectList(MySQLOperation.getProjectList(MySQLOperation.connectionToDatabase()));

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

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Controller.username = username;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Controller.currentUser = currentUser;
    }

    public static void resetID() {
        setFinalProjectList(null);
        setCurrentUser(null);
        setUsername(null);
        projectController.setInitialise(true);
        setSelectedProjectId(0);
        setSelectedIssueId(0);
        setSelectedCommentId(0);
    }

}
