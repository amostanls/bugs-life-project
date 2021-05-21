package home;

import bugs.MySQLOperation;
import bugs.Project;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private static int selectedProjectId=0;
    private static int selectedIssueId=0;
    private static int selectedCommentId=0;
    private static List<Project> finalProjectList;
    private static String username="lim";


    @FXML
    private BorderPane mainPane;

    public static BorderPane staticBorderPane;


    @FXML
    void overview(ActionEvent event) throws IOException {
        Pane view=new FxmlLoader().getPage("dashboard");
        mainPane.setCenter(view);
    }

    @FXML
    void search(MouseEvent event) {

    }

    static void switchToIssues() throws IOException {
        Pane view=new FxmlLoader().getPage("issue");
        staticBorderPane.setCenter(view);
    }

    static void switchToComment(){
        Pane view=new FxmlLoader().getPage("comment");
        staticBorderPane.setCenter(view);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticBorderPane=mainPane;
        Pane view=new FxmlLoader().getPage("dashboard");
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

    /*
    @Override
    protected Task createTask() {
        try{
            setFinalProjectList(MySQLOperation.getProjectList(MySQLOperation.myConnection));
        }catch(Exception e){
            try {
                MySQLOperation.myConnection=MySQLOperation.getConnection();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        finally {
            setFinalProjectList(MySQLOperation.getProjectList(MySQLOperation.myConnection));
        }
        return null;
    }

 */
}
