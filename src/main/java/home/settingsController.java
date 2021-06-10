package home;

import bugs.Mail;
import bugs.MyRunnableEmail;
import bugs.MySQLOperation;
import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static home.Controller.getSelectedCommentId;

public class settingsController implements Initializable {

    private static Service<Void> backGroundThread;

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField usernameDisplay;

    @FXML
    private TextField priviledgeDisplay;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private JFXButton importJsonBtn;

    @FXML
    private JFXButton exportJsonBtn;

    @FXML
    private JFXButton initializeDatabaseBtn;

    @FXML
    void setUpdaterBtn(ActionEvent event) throws InterruptedException {


        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        String sha256hex = DigestUtils.sha256Hex(password);

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
        } else if (password.length() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 8 characters long");
            alert.showAndWait();
        } else if (!password.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Password must be the same");
            alert.showAndWait();
        } else {
            if (Controller.getCurrentUser().getEmail() == null) {//no email provided
                MySQLOperation.updatePassword(MySQLOperation.getConnection(), Controller.getCurrentUser(), sha256hex);
                //JOptionPane.showMessageDialog(null, "Update Successful");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Update Successful");
                alert.showAndWait();
                newPasswordField.clear();
                confirmPasswordField.clear();
            } else {
                MyRunnableEmail runEmail=new MyRunnableEmail(Controller.getCurrentUser().getEmail());
                Thread emailThread=new Thread(runEmail);
                emailThread.start();


                TextInputDialog td = new TextInputDialog();
                td.setTitle("Email verification");
                td.getDialogPane().setHeaderText("Sending email to " + Controller.getCurrentUser().getEmail());
                td.getDialogPane().setContentText("Enter the code sent to your email : ");
                td.showAndWait();
                TextField input = td.getEditor();

                emailThread.join();
                String verificationCode = runEmail.getVerificationCode();

                if (input.getText() != null && input.getText().toString().length() != 0) {
                    if (verificationCode.equals(input.getText())) {
                        //connect to database
                        MySQLOperation.updatePassword(MySQLOperation.getConnection(), Controller.getCurrentUser(), sha256hex);
                        //JOptionPane.showMessageDialog(null, "Update Successful");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Update Successful");
                        alert.showAndWait();
                        newPasswordField.clear();
                        confirmPasswordField.clear();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong code");
                        alert.showAndWait();
                    }

                }
            }

        }

    }

    @FXML
    void importJSON(MouseEvent event) throws Exception {
        ButtonType localButton = new ButtonType("Import from local file");
        ButtonType URLButton = new ButtonType("Import from URL");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", localButton, URLButton);
        alert.setTitle("Import JSON");
        alert.setHeaderText("Please select the type of import you want");
        alert.setContentText("Please select : ");

        Window window = alert.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> alert.hide());
        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(res -> {
            if (res.equals(localButton)) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("JSON Files", "*.json")
                );
                try {

                    File filename = fileChooser.showOpenDialog(((Node) event.getTarget()).getScene().getWindow());
                    //File filename = jfc.getSelectedFile();
                    System.out.println(filename);
                    if (filename != null) {
                        importBackgroundTask(filename);
                    }
                    System.out.println("File name " + filename.getName());

                } catch (NullPointerException e) {
                    System.out.println("No file selected");
                }
            } else if (res.equals(URLButton)) {
                TextInputDialog td = new TextInputDialog();
                td.setTitle("Import JSON");
                td.getDialogPane().setHeaderText("Make sure the values inside JSON file does not conflict with existing data\n\n" +
                        "Instead, you may consider to reset the Database, use Initialise database button");
                td.getDialogPane().setContentText("Enter your file URL : ");
                td.showAndWait();
                TextField input = td.getEditor();
                if (input.getText() != null && input.getText().toString().length() != 0) {
                    try {
                        MySQLOperation.updateDatabaseFromUrl(MySQLOperation.getConnection(), input.getText());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    @FXML
    void exportJSON(MouseEvent event) throws Exception {
        //do stuff

        TextInputDialog td = new TextInputDialog();
        td.setTitle("Export JSON");
        td.getDialogPane().setHeaderText("Make sure the file name is unique");
        td.getDialogPane().setContentText("Enter your file name : ");
        td.showAndWait();
        TextField input = td.getEditor();
        if (input.getText() != null && input.getText().toString().length() != 0) {
            exportBackgroundTask(input.getText());

        }

    }

    @FXML
    void initializeDatabase(MouseEvent event) throws Exception {
        //do stuff
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Initialize Database");
        alert.setHeaderText("You will lose all data, this cannot be undone!");
        alert.setContentText("After finished initializing database, please manually import all data!");

        if (alert.showAndWait().get() == ButtonType.OK) {
            //do stuff
            TextInputDialog td = new TextInputDialog();
            td.setTitle("Reset Database");
            td.getDialogPane().setHeaderText("Type \'CONFIRM\'");
            td.getDialogPane().setContentText("Enter : ");
            td.showAndWait();
            TextField input = td.getEditor();
            if (input.getText() != null && input.getText().toString().length() != 0 && input.getText().equalsIgnoreCase("confirm")) {
                //MySQLOperation.resetDatabase(MySQLOperation.getConnection());
                initializeDatabaseBackgroundTask();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameDisplay.setText(Controller.getCurrentUser().getUsername());
        if (Controller.getCurrentUser().isAdmin()) {
            priviledgeDisplay.setText("Admin");
            enableAdminFunction(true);


        } else {
            priviledgeDisplay.setText("Regular User");
            enableAdminFunction(false);
        }


    }

    private void enableAdminFunction(boolean b) {
        importJsonBtn.setVisible(b);
        exportJsonBtn.setVisible(b);
        initializeDatabaseBtn.setVisible(b);
    }

    private void importBackgroundTask(File filename) {
        backGroundThread = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        MySQLOperation.importJsonFileToDataBase(MySQLOperation.getConnection(), filename);
                        return null;
                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("File Import Complete");
            alert.showAndWait();
        });
        Region veil = new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4);");
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

    private void exportBackgroundTask(String filename) {
        backGroundThread = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        MySQLOperation.exportJavaObjectAsJson(MySQLOperation.getDatabase(MySQLOperation.getConnection()), filename);
                        return null;
                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("File Export Complete");
            alert.showAndWait();
        });
        Region veil = new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4);");
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

    private void initializeDatabaseBackgroundTask() {
        backGroundThread = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        MySQLOperation.resetDatabase(MySQLOperation.getConnection());
                        return null;
                    }
                };
            }
        };
        backGroundThread.setOnSucceeded(workerStateEvent -> {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setHeaderText(null);
            alert1.setContentText("Initialise successful");
            alert1.showAndWait();
        });
        Region veil = new Region();
        veil.setStyle("-fx-background-color:rgba(205,205,205, 0.4);");
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
