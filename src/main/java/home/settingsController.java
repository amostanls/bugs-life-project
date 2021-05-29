package home;

import bugs.MySQLOperation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class settingsController implements Initializable {

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
    void setUpdaterBtn(ActionEvent event) {


        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        //String sha256hex = DigestUtils.sha256Hex(password);

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
            //connect to database
            MySQLOperation.updatePassword(MySQLOperation.connectionToDatabase(), Controller.getCurrentUser(), password);
            //JOptionPane.showMessageDialog(null, "Update Successful");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Update Successful");
            alert.showAndWait();
            newPasswordField.clear();
            confirmPasswordField.clear();

        }

    }

    @FXML
    void importJSON(MouseEvent event) throws Exception {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Import JSON");
        td.getDialogPane().setHeaderText("Make sure the values inside JSON file does not conflict with existing data\n\n" +
                "Instead, you may consider to reset the Database, use Initialise database button");
        td.getDialogPane().setContentText("Enter your file URL : ");
        td.showAndWait();
        TextField input = td.getEditor();
        if (input.getText() != null && input.getText().toString().length() != 0) {
            MySQLOperation.updateDatabaseFromUrl(MySQLOperation.connectionToDatabase(), input.getText());

        }
    }

    @FXML
    void exportJSON(MouseEvent event) throws Exception {
        //do stuff
        Controller.updateTable();
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Export JSON");
        td.getDialogPane().setHeaderText("Make sure the file name is unique");
        td.getDialogPane().setContentText("Enter your file name : ");
        td.showAndWait();
        TextField input = td.getEditor();
        if (input.getText() != null && input.getText().toString().length() != 0) {
            MySQLOperation.exportJavaObjectsAsJson(MySQLOperation.connectionToDatabase(), Controller.getFinalProjectList(), input.getText().toString());

        }

    }

    @FXML
    void initializeDatabase(MouseEvent event) throws Exception {
        //do stuff
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Initialize Database");
        alert.setHeaderText("You will lose all data, this cannot be undone!");
        alert.setContentText("After finished initializing database, you will be automatically logged out!\n" +
                "This function will reset the database using values from : https://jiuntian.com/data.json");

        if (alert.showAndWait().get() == ButtonType.OK) {
            //do stuff
            TextInputDialog td = new TextInputDialog();
            td.setTitle("Reset Database");
            td.getDialogPane().setHeaderText("Make sure to enter the correct name for the database (Case-sensitive)");
            td.getDialogPane().setContentText("Enter the name of the database : ");
            td.showAndWait();
            TextField input = td.getEditor();
            if (input.getText() != null && input.getText().toString().length() != 0) {
                MySQLOperation.resetDatabase(MySQLOperation.connectionToDatabase(), input.getText());
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
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
}
