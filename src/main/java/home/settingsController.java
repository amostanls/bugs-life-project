package home;

import bugs.Mail;
import bugs.MySQLOperation;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

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
                String verificationCode = Mail.resetPassword(Controller.getCurrentUser().getEmail());

                TextInputDialog td = new TextInputDialog();
                td.setTitle("Email verification");
                td.getDialogPane().setHeaderText("Sending email to " + Controller.getCurrentUser().getEmail());
                td.getDialogPane().setContentText("Enter the code sent to your email : ");
                td.showAndWait();
                TextField input = td.getEditor();
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
        JFileChooser jfc = new JFileChooser();
        jfc.showDialog(null, "Please Select the File");
        jfc.setVisible(true);
        try {
            File filename = jfc.getSelectedFile();

            System.out.println("File name " + filename.getName());


            MySQLOperation.importJsonFileToDataBase(MySQLOperation.getConnection(), filename);
        } catch (NullPointerException e) {
            System.out.println("No file selected");
        }

//        TextInputDialog td = new TextInputDialog();
//        td.setTitle("Import JSON");
//        td.getDialogPane().setHeaderText("Make sure the values inside JSON file does not conflict with existing data\n\n" +
//                "Instead, you may consider to reset the Database, use Initialise database button");
//        td.getDialogPane().setContentText("Enter your file URL : ");
//        td.showAndWait();
//        TextField input = td.getEditor();
//        if (input.getText() != null && input.getText().toString().length() != 0) {
//            MySQLOperation.updateDatabaseFromUrl(MySQLOperation.getConnection(), input.getText());
//
//        }
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
            MySQLOperation.exportJavaObjectAsJson(MySQLOperation.getConnection(), MySQLOperation.getDatabase(MySQLOperation.getConnection()), input.getText());

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
                MySQLOperation.resetDatabase(MySQLOperation.getConnection());
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setHeaderText(null);
                alert1.setContentText("Initialise successful");
                alert1.showAndWait();
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
