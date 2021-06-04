package login;

import bugs.*;
import home.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static login.loginController.getResetEmail;

/**
 * @author Tay Qi Xiang on 4/6/2021
 */
public class forgotPasswordController implements Initializable {
    private static User currentResetUser = null;
    private static MyRunnableUsers run = new MyRunnableUsers();
    private static Thread resetThread = new Thread(run);


    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    void setUpdaterBtn(ActionEvent event) throws InterruptedException {
        getUserInfo();

        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        String sha256hex = DigestUtils.sha256Hex(password);



        if (password.isEmpty() || confirmPassword.isEmpty()) {
            errorBox("Please Fill All DATA");

        } else if (password.length() < 8) {
            errorBox("Password must be at least 8 characters long");

        } else if (!password.equals(confirmPassword)) {
            errorBox("Password must be the same");

        } else {

            MySQLOperation.updatePassword(MySQLOperation.getConnection(), currentResetUser, sha256hex);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Update Successful");
            alert.showAndWait();

            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();


        }
        clean();
    }

    private void clean(){

        newPasswordField.clear();
        confirmPasswordField.clear();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resetThread.start();
    }

    private static void getUserInfo() throws InterruptedException {
        resetThread.join();
        List<User> userList = run.getUsers();
        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getEmail()!=null){
                if (userList.get(i).getEmail().equals(getResetEmail())) {
                    currentResetUser = userList.get(i);
                }
            }

        }
    }

    private static void errorBox(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}

