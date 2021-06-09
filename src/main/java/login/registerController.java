package login;

import bugs.MyRunnableEmail;
import bugs.MySQLOperation;

import com.jfoenix.controls.JFXButton;
import home.main;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class registerController implements Initializable {

    private static Service<String> backGroundThread;

    @FXML
    private ChoiceBox<String> userTypeSelection;

    private final String[] userType = {"Regular User", "Admin"};
    private boolean isAdmin = false;
    private final String secretCode = "bugs";


    @FXML
    private TextField emailField;

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
    private StackPane stackPane;


    @FXML
    private JFXButton registerBtn;

    @FXML
    void setLoginBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = new Stage();

        stage.setTitle("Bugs Life");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

    }

    @FXML
    void setRegisterBtn(ActionEvent event) throws InterruptedException {
        String email = emailField.getText();
        String username = usernameField.getText();

        String password = passwordField.getText();
        String confirmPassword = passwordConfirmField.getText();
        String secret = null;
        if (isAdmin) secret = secretField.getText();

        //String sha256hex = DigestUtils.sha256Hex(password);

        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorBox("Please fill in all data");
        } else if (password.length() < 8) {
            errorBox("Password must be at least 8 characters long");
        } else if (!password.equals(confirmPassword)) {
            errorBox("Password must be the same");
        } else if (isAdmin && secret.isEmpty()) {
            errorBox("Please fill in the secret code");
        } else if ((isAdmin && !secret.equals(secretCode))) {
            errorBox("Wrong secret code");
        } else if (!isValidEmail(email)) {
            errorBox("Invalid email address");
        } else if (MySQLOperation.isRegisteredEmail(MySQLOperation.getConnection(), email)) {
            errorBox("This email had been used.\nPlease use another email address");

        } else {
            MyRunnableEmail emailRun = new MyRunnableEmail(email);
            Thread emailThread = new Thread(emailRun);
            emailThread.start();
            //sendCodeBackgroundTask(email);

            TextInputDialog td = new TextInputDialog();
            td.setTitle("Email verification");
            td.getDialogPane().setHeaderText("Sending email to " + email + "\nMake sure the email address is correct");
            td.getDialogPane().setContentText("Enter the code sent to your email : ");
            td.showAndWait();
            TextField input = td.getEditor();
            if (input.getText() != null && input.getText().toString().length() != 0) {
                emailThread.join();
                String verificationCode = emailRun.getVerificationCode();
                if (verificationCode == null) System.out.println("Error!!!");
                if (verificationCode.equals(input.getText())) {
                    String sha256hex = DigestUtils.sha256Hex(password);
                    MySQLOperation.registerUser(MySQLOperation.getConnection(), username, sha256hex, isAdmin, email);
                    //JOptionPane.showMessageDialog(null,"Register Successful");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Register successful");
                    alert.showAndWait();
                    System.out.println("Register successful");
                    //Controller.setUsername(username);

                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                        Stage stage = new Stage();

                        stage.setTitle("Bugs Life");
                        stage.setResizable(false);
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
                    }
                    ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
                } else {
                    errorBox("Wrong verification code");
                }

            }
            //connect to database

        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        secretBox.setVisible(false);
        secretIcon.setVisible(false);
        secretField.setVisible(false);
        userTypeSelection.getItems().addAll(userType);
        userTypeSelection.setValue(userType[0]);
        isAdmin = false;
        userTypeSelection.setOnAction(this::getUserType);
    }

    public void getUserType(ActionEvent event) {
        String user = userTypeSelection.getValue();
        if (user.equals("Admin")) {
            isAdmin = true;
            secretBox.setVisible(true);
            secretIcon.setVisible(true);
            secretField.setVisible(true);
        } else {
            isAdmin = false;
            secretBox.setVisible(false);
            secretIcon.setVisible(false);
            secretField.setVisible(false);
        }
        //System.out.println(isAdmin);
    }

    public static boolean isValidEmail(String email) {
        // create the EmailValidator instance
        EmailValidator validator = EmailValidator.getInstance();

        // check for valid email addresses using isValid method
        return validator.isValid(email);
    }

    private static void errorBox(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }


}


