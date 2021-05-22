package login;

import bugs.MySQLOperation;
import bugs.User;
import com.jfoenix.controls.JFXButton;
import home.Controller;
import home.main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class registerController implements Initializable {

    @FXML
    private ChoiceBox<String> userTypeSelection;

    private String[] userType={"Regular User","Admin"};
    private boolean isAdmin=false;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordConfirmField;

    @FXML
    private HBox secretBox;

    @FXML
    private ImageView secretIcon;

    @FXML
    private PasswordField secretField;

    @FXML
    private JFXButton registerBtn;

    @FXML
    void setLoginBtn(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage=new Stage();

        stage.setTitle("Bugs Life");
        //stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }

    @FXML
    void setRegisterBtn(ActionEvent event) {
        String username=usernameField.getText();

        String password= passwordField.getText();
        String confirmPassword=passwordConfirmField.getText();
        String secret;
        if(isAdmin=true)secret=secretField.getText();

        //String sha256hex = DigestUtils.sha256Hex(password);

        if(username.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()){
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
            //User newUser= MySQLOperation.login(MySQLOperation.connectionToDatabase(),username,password);
            User newUser = null;
            System.out.println("Register successful");
            Controller.setUsername(username);

            try{
                Parent root= FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage=new Stage();

                stage.setTitle("Bugs Life");
                //stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.show();
            }catch(IOException e){
                Logger.getLogger(main.class.getName()).log(Level.SEVERE,null,e);
            }
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        secretBox.setVisible(false);
        secretIcon.setVisible(false);
        secretField.setVisible(false);
        userTypeSelection.getItems().addAll(userType);
        userTypeSelection.setValue(userType[0]);
        userTypeSelection.setOnAction(this::getUserType);
    }

    public void getUserType(ActionEvent event){
        String user=userTypeSelection.getValue();
        if(user.equals("Admin")){
            isAdmin=true;
            secretBox.setVisible(true);
            secretIcon.setVisible(true);
            secretField.setVisible(true);
        }
        else{
            secretBox.setVisible(false);
            secretIcon.setVisible(false);
            secretField.setVisible(false);
        }
    }


}


