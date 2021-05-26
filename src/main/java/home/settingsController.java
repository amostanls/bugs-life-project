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


        String password= newPasswordField.getText();
        String confirmPassword=confirmPasswordField.getText();

        //String sha256hex = DigestUtils.sha256Hex(password);

        if(password.isEmpty()||confirmPassword.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
        }
        else if(password.length()<8){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 8 characters long");
            alert.showAndWait();
        }
        else if(!password.equals(confirmPassword)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Password must be the same");
            alert.showAndWait();
        }
        else{
            //connect to database
            MySQLOperation.updatePassword(MySQLOperation.connectionToDatabase(),Controller.getCurrentUser(),password);
            JOptionPane.showMessageDialog(null,"Update Successful");
            newPasswordField.clear();
            confirmPasswordField.clear();

        }

    }
    @FXML
    void importJSON(MouseEvent event) throws Exception {
        //do stuff
        //MySQLOperation.initializedDatabase();
    }

    @FXML
    void exportJSON(MouseEvent event) throws Exception {
        //do stuff
    }

    @FXML
    void initializeDatabase(MouseEvent event) throws Exception {
        //do stuff
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Initialize Database");
        alert.setHeaderText("You will lose all data, this cannot be undone!");
        alert.setContentText("After finished initialize database, you will be automatically logged out!");

        if(alert.showAndWait().get()== ButtonType.OK){
            //do stuff
            //MySQLOperation.resetDatabase(MySQLOperation.connectionToDatabase());
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
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
