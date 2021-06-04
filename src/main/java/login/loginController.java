package login;

import bugs.*;
import home.Controller;
import home.issuesController;
import home.main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static home.Controller.setFinalProjectList;
import static home.main.myRunnable;
import static home.main.t;
import static login.registerController.isValidEmail;

public class loginController implements Initializable {

    public static String resetEmail="";

    @FXML
    private AnchorPane loginStage;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void setRegisterBtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Stage stage = new Stage();

        stage.setTitle("Bugs Life");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        //new FadeInRight(root).play();
    }

    @FXML
    void setEnterBtn(ActionEvent event) {

        try {
            main.t.join();
            setFinalProjectList(myRunnable.getProjects());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String username = usernameField.getText();

        String password = passwordField.getText();

        String sha256hex = DigestUtils.sha256Hex(password);
        //System.out.println(sha256hex);
        if (username.isEmpty() || password.isEmpty()) {
            errorBox("Please Fill All DATA");

        } else if (password.length() < 8) {
            errorBox("Password must be at least 8 characters long");
        } else {
            //Controller.callBuffer();

            //connect to database
            User currentUser_temp = MySQLOperation.login(MySQLOperation.getConnection(), username, sha256hex);
            if (currentUser_temp != null) {
//                System.out.println("LOGIN successful");
//                System.out.println("Initialising main dashboard");
//                System.out.println("PLease wait");
                Controller.setCurrentUser(currentUser_temp);
                Controller.setUsername(username);

                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/home/main.fxml"));
                    Stage stage = new Stage();

                    stage.setTitle("Bugs Life");
                    stage.setResizable(false);
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setOnCloseRequest(t -> {
                        Platform.exit();
                        System.exit(0);
                    });


                } catch (IOException e) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
                }
                //Controller.closeBuffer();
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();

            } else {
                errorBox("Invalid credentials");

                clean();
            }
        }

    }

    @FXML
    void setForgetBtn(ActionEvent event) throws InterruptedException {
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Email verification");
        td.getDialogPane().setHeaderText("Enter your recovery email address");
        td.getDialogPane().setContentText("Enter your email : ");
        td.showAndWait();
        TextField input = td.getEditor();
        String emailAddress=input.getText();
        if (emailAddress != null && emailAddress.length() != 0) {
            if (isValidEmail(emailAddress)) {
                if (MySQLOperation.isRegisteredEmail(MySQLOperation.getConnection(), emailAddress)) {

                    MyRunnableEmail runEmail=new MyRunnableEmail(emailAddress);
                    Thread emailThread=new Thread(runEmail);
                    emailThread.start();


                    TextInputDialog emailCode = new TextInputDialog();
                    emailCode.setTitle("Email verification");
                    emailCode.getDialogPane().setHeaderText("Sending email to " + emailAddress);
                    emailCode.getDialogPane().setContentText("Enter the code sent to your email : ");

                    emailCode.showAndWait();

                    TextField codeInput = emailCode.getEditor();

                    emailThread.join();
                    String verificationCode = runEmail.getVerificationCode();


                    //System.out.println(codeInput.getText());
                    if (codeInput.getText() != null && codeInput.getText().length() != 0) {
                        if (verificationCode.equals(codeInput.getText())) {
                            try {
                                setResetEmail(emailAddress);
                                Parent parent = FXMLLoader.load(getClass().getResource("forgotPassword.fxml"));
                                Scene scene = new Scene(parent);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                //stage.initStyle(StageStyle.UTILITY);
                                //stage.initStyle(StageStyle.UNDECORATED);
                                stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(issuesController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            errorBox("Wrong verification code");
                        }

                    }
                } else {
                    errorBox("This email is ot found in our database");
                }
            } else {
                errorBox("Invalid Email");
            }

        }
    }

    void clean() {
        usernameField.clear();
        passwordField.clear();
        resetEmail="";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clean();

    }

    private static void errorBox(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public static String getResetEmail() {
        return resetEmail;
    }

    public static void setResetEmail(String resetEmail) {
        loginController.resetEmail = resetEmail;
    }


}

