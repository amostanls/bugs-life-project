package login;

import bugs.MySQLOperation;
import bugs.User;
import home.Controller;
import home.main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class loginController implements Initializable {


    @FXML
    private Button registerBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void setRegisterBtn(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("register.fxml"));
        Stage stage=new Stage();

        stage.setTitle("Bugs Life");
        //stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

    }

    @FXML
    void setEnterBtn(ActionEvent event) {
        String username=usernameField.getText();

        String password= passwordField.getText();

        //String sha256hex = DigestUtils.sha256Hex(password);

        if(username.isEmpty()||password.isEmpty()){
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
        else{
            //connect to database
            User newUser= MySQLOperation.login(MySQLOperation.connectionToDatabase(),username,password);
            if(newUser!=null){
                System.out.println("LOGIN successful");
                Controller.setUsername(username);

                try{
                    Parent root= FXMLLoader.load(getClass().getResource("/home/main.fxml"));
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
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Invalid credentials");
                alert.showAndWait();
                clean();
            }
        }

    }

    void clean(){
        usernameField.clear();
        passwordField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
