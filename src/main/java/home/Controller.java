package home;

import animatefx.animation.FadeInRight;
import bugs.MySQLOperation;
import bugs.Project;
import bugs.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private static int selectedProjectId = 1;
    private static int selectedIssueId = 1;
    private static int selectedCommentId = 1;
    private static List<Project> finalProjectList;
    private static User currentUser;
    private static String username = "";
    private static String urlImage = null;




    @FXML
    private Label usernameDisplay;

    @FXML
    private BorderPane mainPane;

    @FXML
    private ImageView userImage;



    public static BorderPane staticBorderPane;

    @FXML
    void changeImage(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {//Checking double click
            TextInputDialog td = new TextInputDialog();
            td.setTitle("Change user image");
            td.getDialogPane().setHeaderText("Make sure the URL is accessible through the Internet");
            td.getDialogPane().setContentText("Enter the URL of your image : ");
            td.showAndWait();
            TextField input = td.getEditor();
            if (input.getText() != null && input.getText().toString().length() != 0) {
                if (isValidURL(input.getText().toString())) {
                    setUrlImage(input.getText().toString());
                    MySQLOperation.updateUserUrl(MySQLOperation.getConnection(), Controller.getCurrentUser(), input.getText().toString());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Not a valid URL");
                    alert.showAndWait();
                }
            }
        }

        if (urlImage != null) {
            Image image = new Image(urlImage);
            if (!image.isError()) {
                userImage.setImage(image);
            }
        } else {
            Image localImage = new Image(getClass().getResourceAsStream("/images/jimmy-fallon.png"));
            userImage.setImage(localImage);
        }
    }


    @FXML
    void overview(ActionEvent event) throws IOException {
        Pane view = new FxmlLoader().getPage("dashboard");
        mainPane.setCenter(view);
        new FadeInRight(view).play();
    }

    /*@FXML
    void search(MouseEvent event) {
        Pane view = new FxmlLoader().getPage("search");
        mainPane.setCenter(view);
        new FadeInRight(view).play();
    }*/

    @FXML
    void changeLog(MouseEvent event) {
        Pane view = new FxmlLoader().getPage("reportGeneration");
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

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure you want to logout?");

        if (alert.showAndWait().get() == ButtonType.OK) {
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
        userImage.setFitWidth(190);
        userImage.setFitHeight(180);
        userImage.setPreserveRatio(true);
        urlImage = Controller.getCurrentUser().getUrl();
        if (urlImage != null ) {
            Image image = new Image(urlImage);
            if (!image.isError()) {
                userImage.setImage(image);
            }
        } else {
            Image localImage = new Image(getClass().getResourceAsStream("/images/jimmy-fallon.png"));
            userImage.setImage(localImage);
        }

    }


    public static void updateTable() throws Exception {

        setFinalProjectList(MySQLOperation.getProjectList(MySQLOperation.getConnection()));

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

    public static String getUrlImage() {
        return urlImage;
    }

    public static void setUrlImage(String urlImage) {
        Controller.urlImage = urlImage;
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

    public static boolean isValidURL(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }


}
